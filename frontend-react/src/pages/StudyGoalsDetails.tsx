import { useEffect, useState } from "react";
import { useParams, useNavigate } from 'react-router-dom';
import { FaBullseye, FaBook} from "react-icons/fa";
import ReactMarkdown from "react-markdown";
import remarkMath from "remark-math";
import rehypeKatex from "rehype-katex";
import "katex/dist/katex.min.css";
import "../components/Courses.css";
import "../components/StudyGoals.css";

// Enum to choose
type EventType = "EXAM" | "ASSIGNMENT" | "STUDY_GOAL";
type EventStatus = "TODO" | "DONE";

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

function StudyGoalsDetails() {

    const[summary, setSummary] = useState("");
    const [contextSummary, setContextSummary] = useState("");

    const[loading, setLoading] = useState(true); // Loading of page
    const[generating, setGenerating] = useState(true);

    const[error, setError] = useState(""); 
    const navigate =  useNavigate(); // To go back to / (login & request)
    const { id } = useParams();   // Use id parameter of event

    // The study goal event being viewed
    const[event, setEvent] = useState<Event | null>(null);

    // The selection of the tab
    const [tab, setTab] = useState<"summary" | "chat" | "quiz">("summary")

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

        loadEvent();
        loadSummary("");

},[id]);

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
              <span className="loader" />
              <p>Generating your summary ...</p>
            </div>
          </div>
        ) : (
          <>
            <div className="studygoal-summary-card">
              <ReactMarkdown remarkPlugins={[remarkMath]} rehypePlugins={[rehypeKatex]}>
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
      <div className="studygoal-summary-card">
        <h1>Talk with our ai agent</h1>
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