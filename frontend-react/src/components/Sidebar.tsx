import { NavLink } from 'react-router-dom';
import { FiGrid, FiBookOpen, FiHelpCircle, FiUser } from 'react-icons/fi';
import './Sidebar.css';

function Sidebar() {
  return (
    <aside className="sidebar">
      <nav className="sidebar-nav">
        <NavLink
          to="/dashboard"
          className={({ isActive }) => "sidebar-link" + (isActive ? " sidebar-link-active" : "")}
        >
          <FiGrid className="sidebar-link-icon" />
          Dashboard
        </NavLink>

        <NavLink
          to="/courses"
          className={({ isActive }) => "sidebar-link" + (isActive ? " sidebar-link-active" : "")}
        >
          <FiBookOpen className="sidebar-link-icon" />
          Courses
        </NavLink>

        <NavLink
          to="/tutorial"
          className={({ isActive }) => "sidebar-link" + (isActive ? " sidebar-link-active" : "")}
        >
          <FiHelpCircle className="sidebar-link-icon" />
          Tutorial
        </NavLink>
      </nav>

      <NavLink
        to="/profile"
        className={({ isActive }) => "sidebar-link sidebar-link-profile" + (isActive ? " sidebar-link-active" : "")}
      >
        <FiUser className="sidebar-link-icon" />
        Profile
      </NavLink>
    </aside>
  );
}

export default Sidebar;
