import { Link, useNavigate } from 'react-router-dom';
import './Navbar.css';
import logo_inv from '../assets/logo_inv.jpg';

function Navbar() {
  const navigate = useNavigate();

  // Logout: remove token and navigate to root
  function handleLogout() {
    localStorage.removeItem("token");
    navigate("/");
  }

  return (
    <nav className="navbar">
      <Link to="/dashboard">
        <img src={logo_inv} alt="Study Buddy" className="navbar-logo" />
      </Link>

      <div className="navbar-actions">
        <Link className="navbar-link" to="/about">About</Link>
        <button className="navbar-logout" onClick={handleLogout}>
          Log out
        </button>
      </div>
    </nav>
  );
}

export default Navbar;
