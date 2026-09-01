import { useEffect, useRef, useState } from "react";
import { useParams, useNavigate } from 'react-router-dom';
import { FaBullseye, FaBook} from "react-icons/fa";
import { RiUser4Fill } from "react-icons/ri";
import iconAi from '../assets/icon-chat.png';
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import remarkMath from "remark-math";
import rehypeKatex from "rehype-katex";
import "katex/dist/katex.min.css";
import "../components/Courses.css";
import "../components/StudyGoals.css";

// Enum to choose
type EventType = "EXAM" | "ASSIGNMENT" | "STUDY_GOAL";
type EventStatus = "TODO" | "DONE";
type AuthorType = "USER" | "AI"

// Options for the tab
const TAB_OPTIONS = [
  { value: "summary", label: "Summary" },
  { value: "chat", label: "Chat" },
  { value: "quiz", label: "Quiz" },
];

// Inteface that define the object Course received within Event
interface Course {
  id: number;
  name: string;
  color: string;
}

// Inteface that define the object Event received
interface Event {
  id: number;
  title: string;
  eventType: EventType;
  eventStatus: EventStatus;
  date: string;
  course: Course;
}

// Interface that defines the object Message received
interface Message {
    id: number | string;
    author: AuthorType;
    text: string;
}

function StudyGoalsDetails() {

    const[summary, setSummary] = useState("");
    const [contextSummary, setContextSummary] = useState("");

    const[loading, setLoading] = useState(true); // Loading of page
    const[generating, setGenerating] = useState(true); // Generating a summary 
    const[responding, setResponding] = useState(false); // Creating a response 

    const[error, setError] = useState(""); 
    const navigate =  useNavigate(); // To go back to / (login & request)
    const { id } = useParams();   // Use id parameter of event

    // The study goal event being viewed
    const[event, setEvent] = useState<Event | null>(null);

    // The selection of the tab
    const [tab, setTab] = useState<"summary" | "chat" | "quiz">("summary")

    // List of messages received
    const[messages, setMessages] = useState<Message[]>([]);
    const[newMessage, setNewMessage] = useState("");

    // Declaration of anchor for div element
    const bottomRef = useRef<HTMLDivElement>(null);

    useEffect(() => {

        // Get the current event information
        async function loadEvent() {

            const token = localStorage.getItem("token");

            
           try {
            const request = await fetch(`http://localhost:8080/events/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                }
            });

            if (request.ok) {
                const result = await request.json();
                setEvent(result);
                
            } else if (request.status === 401) {
                localStorage.removeItem("token");
                navigate("/");
                
            } else {
                setError(await request.text());
            }

        } catch {
            setError("Could not connect to the server. Please try again.");
        }

        finally {
            setLoading(false);
        }

        }

// Function that loads the entire message history, only runs one time 
async function loadChat() {

        setGenerating(true);
        const token = localStorage.getItem("token");

           try {
            const request = await fetch(`http://localhost:8080/ai/chat/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                }
            });

            if (request.ok) {
                const result = await request.json();
                setMessages(result);
                
            } else if (request.status === 401) {
                localStorage.removeItem("token");
                navigate("/");
                
            } else {
                setError(await request.text());
            }

        } catch {
            setError("Could not connect to the server. Please try again.");
        }

        finally {
            setGenerating(false);
        }

        }

        loadEvent();
        loadSummary("");
        loadChat();

},[id]);

// Run automatically the scroll to end of conversation, everytime messages changes
        useEffect(() => {
            bottomRef.current?.scrollIntoView({behavior: "smooth"})
        }, [messages]);
        

// Function loadsummary with potential context (used when loading page AND regenerate with new context)
async function loadSummary(context: string) {

        setGenerating(true);
        const token = localStorage.getItem("token");

           try {
            const request = await fetch(`http://localhost:8080/ai/summarize/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({context})
            });

            if (request.ok) {
                const result = await request.text();
                setSummary(result);
                
            } else if (request.status === 401) {
                localStorage.removeItem("token");
                navigate("/");
                
            } else {
                setError(await request.text());
            }

        } catch {
            setError("Could not connect to the server. Please try again.");
        }

        finally {
            setGenerating(false);
        }

        }
    
// Send a new message to the AI and append both sides to the chat
async function handleNewMessage(text: string) {

        if (!text.trim()) return;

        const token = localStorage.getItem("token");
        // Add temporarly the message sent with false id for UI
        setMessages((prev) => [...prev, { id: crypto.randomUUID(), author: "USER", text }]);
        setResponding(true);

           try {
            const request = await fetch(`http://localhost:8080/ai/new-message/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "text/plain",
                    "Authorization": "Bearer " + token
                },
                body: text
            });

            if (request.ok) {
                const aiMsg = await request.json();
                setMessages((prev) => [...prev, aiMsg]);

            } else if (request.status === 401) {
                localStorage.removeItem("token");
                navigate("/");

            } else {
                setError(await request.text());
            }

        } catch {
            setError("Could not connect to the server. Please try again.");
        
        } finally {
            setResponding(false);
        }

        }

// Regenerate the summary using the context typed in the form
async function handleRegenerate(e: React.FormEvent) {
    e.preventDefault();
    await loadSummary(contextSummary);
    setContextSummary("");
}


// Loading screen
    if (loading) {
        return <div className="auth-card">Loading...</div>;
    }

    return (
        
        <div className="studygoal-detail-page">

        {error && <p className="auth-error">{error}</p>}

        <div className="studygoal-detail-header">
            <h3> 
                <FaBullseye color= {event?.course.color}/> {event?.title} | 
                <FaBook color= {event?.course.color}/> {event?.course.name}
            </h3>
        </div>

        <div className="status-filter-group">
        {TAB_OPTIONS.map((option) => (
            <button
            key={option.value}
            type="button"
            className={`status-filter-button ${tab === option.value ? "status-filter-button-active" : ""}`}
            onClick={() => setTab(option.value as "summary" | "chat" | "quiz")}
            >
            {option.label}
            </button>
        ))}
        </div>

    {tab === "summary" && (
      <div className="studygoal-tab-panel">
        {generating ? (
          <div className="studygoal-summary-card">
            <div className="studygoal-generating">
              <span className="loader-summary" />
              <p>Generating your summary ...</p>
            </div>
          </div>
        ) : (
          <>
            <div className="studygoal-summary-card">
              <ReactMarkdown remarkPlugins={[remarkGfm, remarkMath]} rehypePlugins={[rehypeKatex]}>
                {summary}
              </ReactMarkdown>
            </div>
            <div className="studygoal-summary-card">
              <form className="studygoal-context-form" onSubmit={handleRegenerate}>
                <textarea
                  className="studygoal-context-input"
                  value={contextSummary}
                  onChange={(e) => setContextSummary(e.target.value)}
                  placeholder="Add additional context and information to adjust the summary"
                />
                <button type="submit" className="course-button">
                  Reformulate
                </button>
              </form>
            </div>
          </>
        )}
      </div>
    )}

    {tab === "chat" && (
      <div className="studygoal-summary-card studygoal-chat">
        <div className="chat-messages">
          {messages.length === 0 ? (
            <div className="chat-row chat-row-ai">
              <img src={iconAi} alt="AI" className="chat-avatar" />
              <div className="chat-bubble">Ask me anything about the topic!</div>
            </div>
          ) : (
            messages.map((msg) => (
              <div
                key={msg.id}
                className={`chat-row ${msg.author === "USER" ? "chat-row-user" : "chat-row-ai"}`}>

                {msg.author === "AI" ? (
                  <img src={iconAi} alt="AI" className="chat-avatar" />
                ) : (
                  <RiUser4Fill className="chat-avatar chat-avatar-user" />
                )}

                <div className="chat-bubble">
                  {msg.author === "AI" ? (
                    <ReactMarkdown remarkPlugins={[remarkGfm, remarkMath]} rehypePlugins={[rehypeKatex]}>
                      {msg.text}
                    </ReactMarkdown>
                  ) : (
                    msg.text
                  )}
                </div>
              </div>
            ))
          )}
        
        {responding && (
        <div className="chat-row chat-row-ai">
            <img src={iconAi} alt="AI" className="chat-avatar" />
            <div className="chat-bubble chat-bubble-typing">
            <span className="loader-chat"/>
            </div>
        </div>
        )}

        <div ref={bottomRef}/>
        
        </div>

        <form
          className="chat-input-row"
          onSubmit={(e) => {
            e.preventDefault();
            handleNewMessage(newMessage);
            setNewMessage("");
          }}
        >
          <textarea
            className="chat-input"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            placeholder="Type your question..."
            rows={1}

            onKeyDown={(e) => {
            if (e.key === "Enter" && !e.shiftKey) { 
                e.preventDefault();             
                handleNewMessage(newMessage);
                setNewMessage("");
                }
            }}
          />
          <button type="submit" className="course-button">Send</button>
        </form>
      </div>
    )}

    {tab === "quiz" && (
      <div className="studygoal-summary-card">
        <h1>Test your knowledge, shine on the exam!</h1>
      </div>
    )}
  </div>
);

    
} export default StudyGoalsDetails;