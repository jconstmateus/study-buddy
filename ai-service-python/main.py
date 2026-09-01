from fastapi import FastAPI
from fastapi.responses import PlainTextResponse
from anthropic import Anthropic
from dotenv import load_dotenv
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

    prompt = f"Explain the topic '{title}' in a clear, informative and concise way for a student. Give example. Be cautious of the limit of tokens (500)"
    if context:
        prompt += f" Remake the actual summary: {summary}; with additional context to focus on: {context}"

    # Create message
    message = client.messages.create(
        model="claude-haiku-4-5-20251001",
        max_tokens=500,
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
