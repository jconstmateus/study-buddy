import { Navigate, Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import Sidebar from './Sidebar';
import './AppLayout.css';

function AppLayout() {
  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className="app-layout">
      <Navbar />
      <div className="app-layout-body">
        <Sidebar />
        <main className="app-layout-content">
          <Outlet />
        </main>
      </div>
      <footer className="app-layout-footer">
        <span>© 2026 Study Buddy</span>
      </footer>
    </div>
  );
}

export default AppLayout;
