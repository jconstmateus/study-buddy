import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import '../components/Courses.css'
import { FaBook, FaRegTrashAlt } from "react-icons/fa";

// Inteface that define the object Course received 
interface Course {
  id: number;
  name: string;
  color: string;
}

function Courses() {
  const [courses, setCourses] = useState<Course[]>([]); // List of Course defined previously, start empty
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [addCourseSubmitting, setAddCourseSubmitting] = useState(false);
  const [nameCourse, setNameCourse] = useState("");
  const [color, setColor] = useState("");
  const [idDelete, setIdDelete] = useState<number | null>(null);

  // LOADING COURSES INFORMATION - useEffect() load automatically without input
  useEffect(() => {
    async function loadCourses() {
      const token = localStorage.getItem("token");

      try {
        const request = await fetch("http://localhost:8080/courses", {
          method: "GET",
          headers: { "Authorization": "Bearer " + token }
        });

        if (request.ok) {
          const result = await request.json();
          setCourses(result); // Receive a list of Courses
        } else {
          setError(await request.text());
        }
      } catch {
        setError("Could not connect to the server. Please try again.");
      } finally {
        setLoading(false);
      }
    }

    loadCourses();
  }, []);

  // Submit handler for adding courses
  async function handleAdd(e: React.FormEvent) {
    e.preventDefault(); // Prevent default response from React to reload page
    setAddCourseSubmitting(true);

    const token = localStorage.getItem("token");

    try {
      const request = await fetch("http://localhost:8080/courses", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ name: nameCourse, color })
      });

      if (request.ok) {
        const result = await request.json();
        setNameCourse("");
        setColor("");
        setCourses([...courses, result]);  // Add new course to the list
      }

    } catch {
      setError("Could not connect to the server. Please try again.");
    } finally {
      setAddCourseSubmitting(false);
    }
  }

  // Loading screen
  if (loading) {
    return <div className="auth-card">Loading...</div>;
  }

  // Click handler for deleting courses
  async function handleDelete(id: number) {
    setIdDelete(id);

    const token = localStorage.getItem("token");

    try {
      const request = await fetch(`http://localhost:8080/courses/${id}`, {
        method: "DELETE",
        headers: {
          "Authorization": "Bearer " + token
        },
      });

      if (request.ok) {
        setCourses(courses.filter((course) => course.id !== id)); // Remove course from the list
      } else {
        setError(await request.text());
      }

    } catch {
      setError("Could not connect to the server. Please try again.");
    } finally {
      setIdDelete(null);
    }
  }

return (
  <div className="courses-page">

    <div className="add-card">
      <h1>My Courses</h1>

      {error && <p className="auth-error">{error}</p>}

      <form onSubmit={handleAdd}>
        <input
          type="text"
          className="course-input"
          placeholder="Course name"
          value={nameCourse}
          onChange={(e) => setNameCourse(e.target.value)}
          required
        />

        <input
          type="color"
          className="course-input-color"
          value={color}
          onChange={(e) => setColor(e.target.value)}
        />

        <button type="submit" className="course-button" disabled={addCourseSubmitting}>
          {addCourseSubmitting ? "Adding..." : "Add Course"}
        </button>
      </form>
    </div>


    <div className="courses-grid">
      {courses.map((course) => (
        <div key={course.id} className="course-card">
          <FaBook style={{ color: course.color }} />
          <span style = {{cursor: "pointer"}}>
            <Link to={`/course/${course.id}`}>
            {course.name}
            </Link>
          </span>
          <div className="course-card-actions">
            <FaRegTrashAlt
              onClick={() => idDelete === null && handleDelete(course.id)}
              style={{
                cursor: idDelete === course.id ? "not-allowed" : "pointer"
              }}
            />
          </div>
        </div>
      ))}
    </div>

  </div>
);
}

export default Courses;