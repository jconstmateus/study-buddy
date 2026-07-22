import { useEffect, useState } from 'react';
import '../Auth.css';

function Profile() {

  // Const to show profile
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
// Start as True for loading screen until getting User
  const [loading, setLoading] = useState(true);

  // Const to change email
  const [newEmail, setNewEmail] = useState("");
  const [passwordEmail, setPasswordEmail] = useState("");
  const [emailSubmitting, setEmailSubmitting] = useState(false);
  const [emailError, setEmailError] = useState("");
  const [emailSuccess, setEmailSuccess] = useState("");

  // Const to change password
  const [newPassword, setNewPassword] = useState("");
  const [passwordPass, setPasswordPass] = useState("");
  const [passwordSubmitting, setPasswordSubmitting] = useState(false);
  const [passwordError, setPasswordError] = useState("");
  const [passwordSuccess, setPasswordSuccess] = useState("");

  // HTTP REQUEST
  // LOADING PROFILE INFORMATION - useEffect() load automatically without input
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


  // Submit handler for the change-email form
  async function handleChangeEmail(e: React.FormEvent) {
    e.preventDefault();
    setEmailError("");
    setEmailSuccess("");
    setEmailSubmitting(true);

    // Load current token to send in the request
    const token = localStorage.getItem("token");

    try {

      // Configure the request
      const request = await fetch("http://localhost:8080/auth/me/change-email", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ password: passwordEmail, newEmail })
      });

      // Receive token and message (error or success)
      if (request.ok) {
        const result = await request.json()

        localStorage.setItem("token", result.token);

        setEmail(newEmail);
        setPasswordEmail("");
        setNewEmail("");
        setEmailSuccess(result.message);

      } else {
        setEmailError(await request.text());
      }

    } catch {
      setEmailError("Could not connect to the server. Please try again.");

    } finally {
      setEmailSubmitting(false);
    }
  }

  // Submit handler for the change-password form
  async function handleChangePassword(e: React.FormEvent) {
    e.preventDefault();
    setPasswordError("");
    setPasswordSuccess("");
    setPasswordSubmitting(true);

   // Load current token to send in the request
    const token = localStorage.getItem("token");

    try {

      // Configure the request
      const request = await fetch("http://localhost:8080/auth/me/change-password", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ password: passwordPass, newPassword })
      });

      // Receive token and message (error or success)
      if (request.ok) {
        const result = await request.json()

        setPasswordPass("");
        setNewPassword("");
        setPasswordSuccess(result.message);

      } else {
        setPasswordError(await request.text());
      }

    } catch {
      setPasswordError("Could not connect to the server. Please try again.");

    } finally {
      setPasswordSubmitting(false);
    }
  }
  
  // Loading screen
  if (loading) {
    return <div className="auth-card">Loading...</div>;
  }

  return (
    <div className="auth-side-by-side">
    <div className="auth-card">
      <h1>Profile</h1>

      {error && <p className="auth-error">{error}</p>}

      {!error && (
        <>
          <label className="auth-label">Name</label>
          <p>{name}</p>

          <label className="auth-label">Email</label>
          <p>{email}</p>
        </>
      )}
    </div>

    <div className="auth-card">
      <h1>Change Email</h1>

      {emailError && <p className="auth-error">{emailError}</p>}
      {emailSuccess && <p className="auth-success">{emailSuccess}</p>}

      <form onSubmit={handleChangeEmail}>
        <label className="auth-label">New Email</label>
        <input
            type="email"
            className="auth-input"
            value={newEmail}
            onChange={(e) => setNewEmail(e.target.value)}
            disabled={emailSubmitting}
            required
        />

        <label className="auth-label">Current Password</label>
        <input
            type="password"
            className="auth-input"
            value={passwordEmail}
            onChange={(e) => setPasswordEmail(e.target.value)}
            disabled={emailSubmitting}
            required
        />

        <button type="submit" className="auth-button" disabled={emailSubmitting}>
          {emailSubmitting ? "Saving..." : "Save"}
        </button>
      </form>
    </div>

    <div className="auth-card">
      <h1>Change Password</h1>

      {passwordError && <p className="auth-error">{passwordError}</p>}
      {passwordSuccess && <p className="auth-success">{passwordSuccess}</p>}

      <form onSubmit={handleChangePassword}>
        <label className="auth-label">New Password</label>
        <input
            type="password"
            className="auth-input"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            disabled={passwordSubmitting}
            required
        />

        <label className="auth-label">Current Password</label>
        <input
            type="password"
            className="auth-input"
            value={passwordPass}
            onChange={(e) => setPasswordPass(e.target.value)}
            disabled={passwordSubmitting}
            required
        />

        <button type="submit" className="auth-button" disabled={passwordSubmitting}>
          {passwordSubmitting ? "Saving..." : "Save"}
        </button>
      </form>
    </div>
    </div>
  );

}

export default Profile;
