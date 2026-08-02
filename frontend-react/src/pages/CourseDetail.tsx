import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import '../components/Courses.css'
import { FaBook, FaRegTrashAlt, FaPen } from "react-icons/fa";

function CourseDetail() {

    const { id } = useParams();
    const[nameCourse, setNameCourse] = useState("");
    const[colorCourse, setColorCourse] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const [editing, setEditing] = useState(false);

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

    loadCourse();

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

    </div>
    )

} export default CourseDetail;