import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../components/Courses.css";
import "../components/StudyGoals.css";
import { FaBullseye, FaRegTrashAlt } from "react-icons/fa";
import { MdCheckBoxOutlineBlank, MdCheckBox } from "react-icons/md";

type EventType = "EXAM" | "ASSIGNMENT" | "STUDY_GOAL";
type EventStatus = "TODO" | "DONE";
type StatusFilter = "ALL" | "TODO" | "DONE" | "OVERDUE";

const STATUS_FILTER_OPTIONS: { value: StatusFilter; label: string }[] = [
  { value: "ALL", label: "All" },
  { value: "TODO", label: "To do" },
  { value: "DONE", label: "Done" },
  { value: "OVERDUE", label: "Overdue"},
];

// Inteface that define the object Course
interface Course {
  id: number;
  name: string;
  color: string;
}

// Inteface that define the object Event
interface Event {
  id: number;
  title: string;
  eventType: EventType;
  eventStatus: EventStatus;
  date: string;
  course: Course;
}

function StudyGoals() {

    const [studyGoals, setStudyGoals] = useState<Event[]>([]);
    const [courses, setCourses] = useState<Course[]>([]);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    // Param. to create a new study goal
    const [titleGoal, setTitleGoal] = useState("");
    const [courseIdGoal, setCourseIdGoal] = useState("");
    const [dateGoal, setDateGoal] = useState("");
    const [addSubmitting, setAddSubmitting] = useState(false);

    // Filter the list by course and by status
    const [filterCourseId, setFilterCourseId] = useState("");
    const [statusFilter, setStatusFilter] = useState<StatusFilter>("ALL");


    useEffect(() => {

        async function loadStudyGoals() {
            const token = localStorage.getItem("token");

       try {
        const request = await fetch(`http://localhost:8080/events?type=STUDY_GOAL`, {
          method: "GET",
          headers: { "Authorization": "Bearer " + token }
        });

         if (request.ok) {
          const result = await request.json();
          setStudyGoals(result);

        } else if (request.status === 401) {
          localStorage.removeItem("token");
          navigate("/");

        } else {
          const result = await request.text();
          setError(result);
        }

       } catch {
            setError("Could not connect to the server. Please try again.");

       } finally {
        setLoading(false);
       }
    }

    // Get all the courses for the dropdown
    async function loadCourses() {
      const token = localStorage.getItem("token");

      try {
        const request = await fetch("http://localhost:8080/courses", {
          method: "GET",
          headers: { "Authorization": "Bearer " + token }
        });

        if (request.ok) {
          const result = await request.json();
          setCourses(result);

        } else if (request.status === 401) {
          localStorage.removeItem("token");
          navigate("/");

        } else {
          setError(await request.text());
        }
      } catch {
        setError("Could not connect to the server. Please try again.");
      }
    }

    loadStudyGoals();
    loadCourses();

    },[]);

    // Loading screen
    if (loading) {
        return <div className="auth-card">Loading...</div>;
    }

    // Save handler for adding a study goal (creates the underlying Event)
    async function handleAddStudyGoal(e: React.FormEvent) {
        e.preventDefault(); // Prevent default response from React to reload page
        setAddSubmitting(true);

        const token = localStorage.getItem("token");

        try {
            const request = await fetch(`http://localhost:8080/courses/${courseIdGoal}/events`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({
                    title: titleGoal,
                    eventType: "STUDY_GOAL",
                    eventStatus: "TODO",
                    date: `${dateGoal}T23:59`
                })
            });

            if (request.ok) {
                const result = await request.json();
                setStudyGoals([...studyGoals, result]); // Add new study goal to the list
                setTitleGoal("");
                setCourseIdGoal("");
                setDateGoal("");

            } else if (request.status === 401) {
                localStorage.removeItem("token");
                navigate("/");
            } else {
                setError(await request.text());
            }
        } catch {
            setError("Could not connect to the server. Please try again.");
        } finally {
            setAddSubmitting(false);
        }
    }

  // Changing handler for updating status of a study goal
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

      setStudyGoals(studyGoals.map((e) =>
      e.id === eventId ? { ...e, eventStatus: result as EventStatus } : e
    ));

    } else if (request.status === 401) {
      localStorage.removeItem("token");
      navigate("/");
    } else {
      setError(await request.text());
    }

    } catch {
      setError("Could not connect to the server. Please try again.");
    }
  }


   // Delete handler for deleting a study goal
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

      setStudyGoals(studyGoals.filter((event) => event.id !== eventId)); // Remove study goal from the list

    } else if (request.status === 401) {
      localStorage.removeItem("token");
      navigate("/");
    } else {
      setError(await request.text());
    }

    } catch {
      setError("Could not connect to the server. Please try again.");
    }
  }

return (
    <div className="study-goals-page">
      <h1 className="study-goals-title">My Study Goals</h1>
      {error && <p className="auth-error">{error}</p>}

      <div className="courses-page">

        {/* Card for adding a new studygoal */}
        <div className="add-card">
          <h1>Add Study Goal</h1>
          
          <form onSubmit={handleAddStudyGoal}>
            <input
              type="text"
              className="course-input"
              placeholder="Study goal title"
              value={titleGoal}
              onChange={(e) => setTitleGoal(e.target.value)}
              required
            />

            <select
              className="course-input"
              value={courseIdGoal}
              onChange={(e) => setCourseIdGoal(e.target.value)}
              required
            >
              <option value="" disabled>Select a course</option>
              {courses.map((course) => (
                <option key={course.id} value={course.id}>{course.name}</option>
              ))}
            </select>

            <input
              type="date"
              className="course-input"
              value={dateGoal}
              onChange={(e) => setDateGoal(e.target.value)}
              required
            />

            <button type="submit" className="course-button" disabled={addSubmitting}>
              {addSubmitting ? "Adding..." : "Add Study Goal"}
            </button>
          </form>
        </div>
        
        {/* Dropdown to filter the respective course */}
        <div className="event-board">
          <div className="study-goals-filters">
            <select
              className="course-input study-goals-filter"
              value={filterCourseId}
              onChange={(e) => setFilterCourseId(e.target.value)}
            >
              <option value="">All courses</option>
              {courses.map((course) => (
                <option key={course.id} value={course.id}>{course.name}</option>
              ))}
            </select>
            
            {/* Buttons to filter events by status */}
            <div className="status-filter-group">
              {STATUS_FILTER_OPTIONS.map((option) => (
                <button
                  key={option.value}
                  type="button"
                  className={`status-filter-button ${statusFilter === option.value ? "status-filter-button-active" : ""}`}
                  onClick={() => setStatusFilter(option.value)}
                >
                  {option.label}
                </button>
              ))}
            </div>
          </div>  
          
          {/* Filter and sort events with selection chosen */}
          <div className="event-row">
          {studyGoals.length === 0 ? (
            <p className="event-empty">No study goals yet.</p>
          ) : (
            studyGoals
              .filter((event) => filterCourseId === "" || event.course.id === Number(filterCourseId))
              .filter((event) => {
                if (statusFilter === "ALL") return true;
                if (statusFilter === "OVERDUE") return event.eventStatus === "TODO" && new Date(event.date) < new Date();
                return event.eventStatus === statusFilter;
              })
              .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
              .map((event) => {
              const isOverdue = event.eventStatus === "TODO" && new Date(event.date) < new Date();
              
              //* Presentation of the events accordingly
              return (
        
                <div
                  key={event.id}
                  className={`course-card ${event.eventStatus === "DONE" ? "course-card-done" : ""} ${isOverdue ? "course-card-overdue" : ""}`}
                >
                  {isOverdue && <span className="event-overdue-badge">Overdue</span>}
                  <FaBullseye style={{ color: event.course.color }} />
                  <Link to={`/study-goals/${event.id}`}>
                  <span>{event.title}</span>
                   </Link>
                  <span>{event.course.name}</span>
                  <span
                    onClick={() => handleChangeStatus(event.id, event.eventStatus === "DONE" ? "TODO" : "DONE")}
                    style={{ cursor: "pointer" }}
                  >
                    {event.eventStatus === "DONE" ? <MdCheckBox /> : <MdCheckBoxOutlineBlank />}
                  </span>
                  <span>{new Date(event.date).toLocaleDateString()}</span>
                  <div className="course-card-actions" onClick={() => handleDelete(event.id)}>
                    <FaRegTrashAlt />
                  </div>
                </div>
         
              );
            })
          )}
          </div>
        </div>

      </div>
    </div>
);

} export default StudyGoals;
