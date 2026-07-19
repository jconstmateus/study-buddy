import '../Auth.css';
import logo from '../assets/logo.jpg';
import Login from '../components/Login';
import Register from '../components/Register';


function Authpage() {
    return(
    <div className="auth-shell">
        <div className="auth-brand">
            <img src={logo} alt="Study Buddy" className="auth-brand-logo" />
        </div>

        <div className="auth-side-by-side">
            <Login />
            <Register />
        </div>

        <footer className="auth-footer">
            <a>About</a>
            <span>© 2026 Study Buddy</span>
        </footer>
        </div>
    )
}

export default Authpage;