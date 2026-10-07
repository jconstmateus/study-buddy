from fastapi import FastAPI, HTTPException
from fastapi.responses import PlainTextResponse
from anthropic import Anthropic
from dotenv import load_dotenv
import json
import os

load_dotenv() # Load virtual variables

app = FastAPI() 
client = Anthropic(api_key=os.getenv("ANTHROPIC_API_KEY"))

# POST (create a summary with title and potential context)
@app.post("/summarize", response_class=PlainTextResponse)
def summarize(data: dict):
    title = data["title"]
    context = data.get("context", "")
    summary = data.get("summary", "")

    prompt = f"Explain the topic '{title}' in a clear, informative and concise way for a student. Give example. Be cautious of the limit of tokens (700)"
    if context:
        prompt += f" Remake the actual summary: {summary}; with additional context to focus on: {context}"

    # Create message
    message = client.messages.create(
        model="claude-haiku-4-5-20251001",
        max_tokens=1000,
        messages=[
            {"role": "user", "content": prompt}
        ]
    )
     
    # Return first and only message
    return message.content[0].text


# POST (create a response with chat and summary)
@app.post("/new-message", response_class=PlainTextResponse)
def new_message(data: dict):
    summary = data.get("summary", "")
    chat = data.get("chat", [])

    messages = []
    
    for m in chat:

        if m["author"] == "USER":
            role = "user"
        else:
            role = "assistant"
        
        messages.append({"role": role, "content": m["text"]})

    message = client.messages.create(
        model="claude-haiku-4-5-20251001",
        max_tokens=500,
        system=(
            "You are helping a student study. Here is the topic summary for context:\n\n"
            f"{summary}\n\n"
            "Now, answer their questions clearly, informatively and concisely."
        ),
        messages=messages,
    )
    return message.content[0].text


# POST (create new questions for a quizz)
@app.post("/generate-questions") 
def generateQuestions(data: dict):
    summary = data.get("summary", "")

    message = client.messages.create(
        model="claude-haiku-4-5-20251001",
        max_tokens=1500,
        system=(
            "You are creating a quiz based on this summary:\n\n"
            f"{summary}\n\n"
            "options must be a single string with the 4 choices separated by ', '.\n"
            "correctAnswer must be exactly equal to one of those 4 choices."
        ),
        messages=[
            {"role": "user", "content": "Generate a quiz with 5 questions, each should have ONLY 4 options each and ONLY ONE must be a correct answer."}],
        output_config={ # Create a fixed schema to return
            "format": {
                "type": "json_schema",
                "schema": {
                    "type": "object",
                    "properties": {
                        "questions": {
                            "type": "array",
                            "items": {
                                "type": "object",
                                "properties": {
                                    "statement": {"type": "string"},
                                    "options": {"type": "string"},
                                    "correctAnswer": {"type": "string"}
                                },
                                "required": ["statement", "options", "correctAnswer"],
                                "additionalProperties": False
                            }
                        }
                    },
                    "required": ["questions"],
                    "additionalProperties": False
                }
            }
        }
    )

    text = message.content[0].text
    try:
        result = json.loads(text)
    except json.JSONDecodeError:
        raise HTTPException(status_code=502, detail=f"AI returned invalid JSON")

    return result["questions"]





    
