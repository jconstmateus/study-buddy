import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../Auth.css';

function Login() {

  const navigate = useNavigate();

  // Tuples to update
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  // HTTP REQUEST
  async function handleLogin(e: React.FormEvent) {

    // Prevent refreshing webpage
    e.preventDefault();

    setLoading(true);

    try {
      // Configure the request
      const request = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
      });

      // Receive token or error
      const result = await request.text();

      // Receive 200-success and register token
      if (request.ok) {
        localStorage.setItem("token", result);
        setError("");
        navigate("/profile");

      // Error - update the error
      } else {
        setError(result);
      }

    } catch {
      setError("Could not connect to the server. Please try again.");

    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-card">
      <h1>Log in</h1>
      <p className="auth-subtitle">Welcome back to Study Buddy.</p>
      <form onSubmit={handleLogin}>
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
          {loading ? "Logging in..." : "Log in"}
        </button>

        {error && <p className="auth-error">{error}</p>}
      </form>
    </div>
  );

}

export default Login;

