import { useState } from 'react';
import '../Auth.css';

function Register() {

  // Tuples to update
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  // HTTP Request
  async function handleRegister(e: React.FormEvent) {

    // Prevent refreshing webpage
    e.preventDefault();

    setLoading(true);

    try {
      // Configure the request
      const request = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password })
      });

      // Receive token or error
      const result = await request.text();

      // Receive 200-success and register token
      if (request.ok) {
        localStorage.setItem("token", result);
        setError("");
        setSuccess("Account created successfully!");

        // Error - update the error
      } else {
        setError(result);
        setSuccess("");
      }
    } catch {
      setError("Could not connect to the server. Please try again.");
      setSuccess("");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-card">
      <h1>Sign up</h1>
      <p className="auth-subtitle">Create your Study Buddy account.</p>
      <form onSubmit={handleRegister}>
        <label className="auth-label">Name</label>
        <input
          type="text"
          className="auth-input"
          value={name}
          onChange={(e) => setName(e.target.value)}
          disabled={loading}
          required
        />

        <label className="auth-label">Email</label>
        <input
          type="email"
          className="auth-input"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          disabled={loading}
          required
        />

        <label className="auth-label">Password</label>
        <input
          type="password"
          className="auth-input"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          disabled={loading}
          required
        />

        <button type="submit" className="auth-button" disabled={loading}>
          {loading ? "Creating account..." : "Create account"}
        </button>

        {error && <p className="auth-error">{error}</p>}
        {success && <p className="auth-success">{success}</p>}
      </form>
    </div>
  );

}

export default Register;