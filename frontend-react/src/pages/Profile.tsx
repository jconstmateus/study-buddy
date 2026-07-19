import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../Auth.css';

function Profile() {

  const navigate = useNavigate();

  // Tuples to update
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
// Start as True for loading screen until getting User
  const [loading, setLoading] = useState(true); 

  // HTTP REQUEST
  useEffect(() => {

    async function loadProfile() {
    // Get token from storage
      const token = localStorage.getItem("token");

      try {

        // Configure the request
        const request = await fetch("http://localhost:8080/auth/me", {
          method: "GET",
          headers: { "Authorization": "Bearer " + token }
        });

        // Receive user or error
        if (request.ok) {
          const result = await request.json();
          setName(result.name);
          setEmail(result.email);

        // Error - update the error
        } else {
          setError(await request.text());
        }

      } catch {
        setError("Could not connect to the server. Please try again.");

      } finally {
        setLoading(false);
      }
    }

    loadProfile();
  }, []);


  // Function to handle navigation and removal of token when loggout
  function handleLogout() {
    localStorage.removeItem("token");
    navigate("/");
  }

  // Loading screen 
  if (loading) {
    return <div className="auth-card">Loading...</div>;
  }

  return (
    <div className="auth-card">
      <h1>Profile</h1>

      {error && <p className="auth-error">{error}</p>}

      {!error && (
        <>
          <label className="auth-label">Name</label>
          <p>{name}</p>

          <label className="auth-label">Email</label>
          <p>{email}</p>

          <button className="auth-button" onClick={handleLogout}>
            Log out
          </button>
        </>
      )}
    </div>
  );

}

export default Profile;
