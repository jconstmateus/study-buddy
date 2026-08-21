import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Calendar, dateFnsLocalizer, Views } from 'react-big-calendar';
import type { View } from 'react-big-calendar';
import { format, parse, startOfWeek, getDay } from 'date-fns';
import { enUS } from 'date-fns/locale';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import '../components/Dashboard.css';
import { MdCheckBoxOutlineBlank, MdCheckBox } from 'react-icons/md';
import { FaRegTrashAlt, FaGraduationCap, FaTasks, FaBullseye } from "react-icons/fa";
import type { IconType } from "react-icons";

// Enum to choose
type EventType = "EXAM" | "ASSIGNMENT" | "STUDY_GOAL";
type EventStatus = "TODO" | "DONE";

// One icon + label per event type
const EVENT_TYPE_CONFIG: { type: EventType; label: string; icon: IconType }[] = [
  { type: "EXAM", label: "Exams", icon: FaGraduationCap },
  { type: "ASSIGNMENT", label: "Assignments", icon: FaTasks },
  { type: "STUDY_GOAL", label: "Study Goals", icon: FaBullseye },
];

// Inteface that define the object Course 
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

// Connect the date-fns library to react-big-calendar
const localizer = dateFnsLocalizer({
  format,
  parse,
  startOfWeek: () => startOfWeek(new Date(), { locale: enUS }),
  getDay,
  locales: { "en-US": enUS },
});

function Dashboard() {

    const navigate = useNavigate();
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const [date, setDate] = useState(new Date());
    const [view, setView] = useState<View>(Views.MONTH);

    const [events, setEvents] = useState<Event[]>([]); // Empty list of Events

    useEffect(() => {

      async function loadEvents() {
       const token = localStorage.getItem("token");

       try {
        const request = await fetch(`http://localhost:8080/events`, {
          method: "GET",
          headers: { "Authorization": "Bearer " + token }
        });

         if (request.ok) {
          const result = await request.json();
          setEvents(result);

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
    loadEvents();

    },[]);

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

    // Loading screen
    if (loading) {
        return <div className="auth-card">Loading...</div>;
    }



    // Convert our Events into the { title, start, end } 
    // shape react-big-calendar expects (Date object)
    const calendarEvents = events.map((event) => ({
  id: event.id,
  title: event.title,
  courseName: event.course.name,
  eventType: event.eventType,
  start: new Date(event.date),
  end: new Date(event.date),
  color: event.course.color,
  status: event.eventStatus,
}));

  return (
    <div className="dashboard-page">
      <h1 className="dashboard-title">My Dashboard</h1>
      {error && <p className="auth-error">{error}</p>}

      <div className="dashboard-calendar">
      <Calendar
        localizer={localizer}
        events={calendarEvents}
        startAccessor="start"
        endAccessor="end"
        style={{ height: "100%" }}
        popup
        date={date}
        view={view}
        onNavigate={(newDate) => setDate(newDate)}
        onView={(newView) => setView(newView)}
        eventPropGetter={(event) => ({
          style: { backgroundColor: event.color },
        })}
        components={{
          event: ({ event }) => {
            const Icon = EVENT_TYPE_CONFIG.find((c) => c.type === event.eventType)?.icon;

            return (
            <div className="calendar-event-content">
              <span className="calendar-event-title">
                {Icon && <Icon className="calendar-event-icon" />}
                <span className="calendar-event-title-text">
                  {event.title}
                  <span className="calendar-event-course-inline"> · {event.courseName}</span>
                </span>
              </span>
              <div className="calendar-event-footer">
                <span className="calendar-event-course">{event.courseName}</span>
                <div className="calendar-event-actions">
                  <span
                    className="calendar-event-delete"
                    onClick={() => handleDelete(event.id)}
                  >
                    <FaRegTrashAlt />
                  </span>
                  <span
                    className="calendar-event-checkbox"
                    onClick={() => handleChangeStatus(event.id, event.status === "DONE" ? "TODO" : "DONE")}
                  >
                    {event.status === "DONE" ? <MdCheckBox /> : <MdCheckBoxOutlineBlank />}
                  </span>
                </div>
              </div>
            </div>
            );
          },
        }}
      />
      </div>
    </div>
  );

}

export default Dashboard;
