import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import '../components/Courses.css'
import { FaBook, FaPen, FaGraduationCap, FaTasks, FaBullseye, FaRegTrashAlt } from "react-icons/fa";
import type { IconType } from "react-icons";
import { MdCheckBoxOutlineBlank, MdCheckBox } from 'react-icons/md'

// Enum to choose
type EventType = "EXAM" | "ASSIGNMENT" | "STUDY_GOAL";
type EventStatus = "TODO" | "DONE";

// Inteface that define the object Event received
interface Event {
  id: number;
  title: string;
  eventType: EventType;
  eventStatus: EventStatus;
  date: string;
}

// One icon + label per event type
const EVENT_TYPE_CONFIG: { type: EventType; label: string; icon: IconType }[] = [
  { type: "EXAM", label: "Exams", icon: FaGraduationCap },
  { type: "ASSIGNMENT", label: "Assignments", icon: FaTasks },
  { type: "STUDY_GOAL", label: "Study Goals", icon: FaBullseye },
];

// One icon per status 
const EVENT_STATUS_CONFIG: { status: EventStatus; icon: IconType }[] = [
  { status: "TODO", icon: MdCheckBoxOutlineBlank },
  { status: "DONE", icon: MdCheckBox },
];

function CourseDetail() {

    const { id } = useParams();
    const[nameCourse, setNameCourse] = useState("");
    const[colorCourse, setColorCourse] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const [editing, setEditing] = useState(false);
    const [events, setEvents] = useState<Event[]>([]); // List of Events defined previously, starts empty

    // Param. to create new event
    const[titleEvent, setTitleEvent] = useState("");
    const[typeEvent, setTypeEvent] = useState<EventType | "">("");
    const[statusEvent, setEventStatus] = useState<EventStatus>("TODO");
    const[dateEvent, setDateEvent] = useState("");
    const [addEventSubmitting, setAddEventSubmitting] = useState(false);


    // LOADING COURSE INFORMATION - useEffect() load automatically without input
  useEffect(() => {
    async function loadCourse() {
       const token = localStorage.getItem("token");

       try {
        const request = await fetch(`http://localhost:8080/courses/${id}`, {
          method: "GET",
          headers: { "Authorization": "Bearer " + token }
        });

        if (request.ok) {
            const result = await request.json()
            setNameCourse(result.name);
            setColorCourse(result.color);
        } else {
             setError(await request.text());
        }
           
       } catch {
            setError("Could not connect to the server. Please try again.");

       } finally {
        setLoading(false);
       }
    }

    async function loadEvents() {
       const token = localStorage.getItem("token");

       try {
        const request = await fetch(`http://localhost:8080/events/${id}`, {
          method: "GET",
          headers: { "Authorization": "Bearer " + token }
        });

        if (request.ok) {
          const result = await request.json();
          setEvents(result); // Receive a list of Events
            
        } else {
             setError(await request.text());
        }
           
       } catch {
            setError("Could not connect to the server. Please try again.");

       } finally {
        setLoading(false);
       }
    }

    loadCourse();
    loadEvents();

    }, [id]);

    // Loading screen
    if (loading) {
        return <div className="auth-card">Loading...</div>;
    }

    // Save handler for editing the course
    async function handleSave() {
        const token = localStorage.getItem("token");

        try {
            const request = await fetch(`http://localhost:8080/courses/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({ name: nameCourse, color: colorCourse })
            });

            if (request.ok) {
                const result = await request.json();
                setNameCourse(result.name);
                setColorCourse(result.color);
                setEditing(false);
            } else {
                setError(await request.text());
            }
        } catch {
            setError("Could not connect to the server. Please try again.");
        }
    }

    // Save handler for adding an event
    async function handleAddEvent(e: React.FormEvent) {
        e.preventDefault(); // Prevent default response from React to reload page
        setAddEventSubmitting(true);

        const token = localStorage.getItem("token");

        try {
            const request = await fetch(`http://localhost:8080/events/by-course/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({
                    title: titleEvent,
                    eventType: typeEvent,
                    eventStatus: statusEvent,
                    // EXAM already carries a time (datetime-local); other types are date-only, default to end of day
                    date: typeEvent === "EXAM" ? dateEvent : `${dateEvent}T23:59`
                })
            });

            if (request.ok) {
                const result = await request.json();
                setEvents([...events, result]); // Add new event to the list
                setTitleEvent("");
                setTypeEvent("");
                setDateEvent("");
            } else {
                setError(await request.text());
            }
        } catch {
            setError("Could not connect to the server. Please try again.");
        } finally {
            setAddEventSubmitting(false);
        }
    }

  // Changing handler for updating status of event
  async function handleChangeStatus(eventId: number, newStatus: EventStatus) {
    const token = localStorage.getItem("token");

    try {
      const request = await fetch(`http://localhost:8080/events/${eventId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ 
          newEventStatus: newStatus 
        })
      });

     
      if (request.ok) {
      const result = await request.json();

      setEvents(events.map((e) =>
      e.id === eventId ? { ...e, eventStatus: result as EventStatus } : e
    ));

    } else {
      setError(await request.text());
    }

    } catch {
      setError("Could not connect to the server. Please try again.");
    }
  }


   // Delete handler for deleting event
  async function handleDelete(eventId: number) {
    const token = localStorage.getItem("token");

    try {
      const request = await fetch(`http://localhost:8080/events/${eventId}`, {
        method: "DELETE",
        headers: {
          "Authorization": "Bearer " + token
        },
      });

     
      if (request.ok) {

      setEvents(events.filter((event) => event.id !== eventId)); // Remove course from the list

    } else {
      setError(await request.text());
    }

    } catch {
      setError("Could not connect to the server. Please try again.");
    }
  }


    return (
    <div className="courses-detail-page">

    <div className="course-detail-header">
    <div className='class-button'>
      <Link to="/courses">
        <button className="course-button">Return to Courses</button>
      </Link>
    </div>

    <div className="course-detail-card">
      {editing ? (
        <div className="course-edit-row">
          <input
            type="text"
            className="course-input"
            value={nameCourse}
            onChange={(e) => setNameCourse(e.target.value)}
          />
          <input
            type="color"
            className="course-input-color"
            value={colorCourse}
            onChange={(e) => setColorCourse(e.target.value)}
          />
        </div>
      ) : (
        <h1>
          <FaBook style={{ color: colorCourse, marginRight: "8px" }} />
          <span>{nameCourse}</span>
        </h1>
      )}

      {error && <p className="auth-error">{error}</p>}

      <div className="course-card-actions">
        {editing ? (
          <button type="button" className="course-button" onClick={handleSave}>Save</button>
        ) : (
          <FaPen onClick={() => setEditing(true)} />
        )}
      </div>
    </div>
    </div>


    <div className="courses-page">

      <div className="add-card">
        <h1>Add Event</h1>

        {error && <p className="auth-error">{error}</p>}

        <form onSubmit={handleAddEvent}>
          <input
            type="text"
            className="course-input"
            placeholder="Event title"
            value={titleEvent}
            onChange={(e) => setTitleEvent(e.target.value)}
            required
          />

          <select
            className="course-input"
            value={typeEvent}
            onChange={(e) => {
              setTypeEvent(e.target.value as EventType);
              setDateEvent(""); // date/datetime formats aren't compatible, avoid leftover values
            }}
            required
          >
            <option value="" disabled>Select a type</option>
            <option value="EXAM">Exam</option>
            <option value="ASSIGNMENT">Assignment</option>
            <option value="STUDY_GOAL">Study goal</option>
          </select>

          {typeEvent === "EXAM" ? (
            <input
              type="datetime-local"
              className="course-input"
              value={dateEvent}
              onChange={(e) => setDateEvent(e.target.value)}
              required
            />
          ) : (
            <input
              type="date"
              className="course-input"
              value={dateEvent}
              onChange={(e) => setDateEvent(e.target.value)}
              required
            />
          )}

          <button type="submit" className="course-button" disabled={addEventSubmitting}>
            {addEventSubmitting ? "Adding..." : "Add Event"}
          </button>
        </form>
      </div>

      <div className="event-board">
        {EVENT_TYPE_CONFIG.map(({ type, label, icon: Icon }) => {
          const typeEvents = events
            .filter((event) => event.eventType === type)
            .sort((a, b) => {
              if (a.eventStatus === b.eventStatus) return 0;
              return a.eventStatus === "DONE" ? 1 : -1;
            });

          return (
            <div className="event-type-section" key={type}>
              <h2><Icon /> {label}</h2>

              <div className="event-row">
                {typeEvents.length === 0 ? (
                  <p className="event-empty">No events yet.</p>
                ) : (
                  typeEvents.map((event) => {
                    const isOverdue = event.eventStatus === "TODO" && new Date(event.date) < new Date();

                    return (
                    <div
                      key={event.id}
                      className={`course-card ${event.eventStatus === "DONE" ? "course-card-done" : ""} ${isOverdue ? "course-card-overdue" : ""}`}
                    >
                      {isOverdue && <span className="event-overdue-badge">Overdue</span>}
                      <Icon style={{ color: colorCourse }} />
                      <span>{event.title}</span>
                      <span onClick={() => handleChangeStatus(event.id, event.eventStatus === "DONE" ? "TODO" : "DONE")} style={{ cursor: "pointer" }}>
                      {event.eventStatus === "DONE" ? <MdCheckBox /> : <MdCheckBoxOutlineBlank />}
                      </span>
                      <span>
                        {event.eventType === "EXAM"
                          ? new Date(event.date).toLocaleString()
                          : new Date(event.date).toLocaleDateString()}
                      </span>
                      <div className="course-card-actions" onClick={() => handleDelete(event.id)}>
                        <FaRegTrashAlt />
                      </div>
                    </div>
                    );
                  })
                )}
              </div>
            </div>
          );
        })}
      </div>

    </div>

    </div>
    )

} export default CourseDetail;