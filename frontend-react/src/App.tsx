import { Routes, Route } from 'react-router-dom';
import Authpage from './pages/Authpage';
import Profile from './pages/Profile';
import Dashboard from './pages/Dashboard';
import Courses from './pages/Courses';
import CourseDetail from './pages/CourseDetail';
import About from './pages/About';
import Tutorial from './pages/Tutorial';
import StudyGoals from './pages/StudyGoals';
import StudyGoalsDetails from './pages/StudyGoalsDetails';
import AppLayout from './components/AppLayout';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Authpage />} />
      <Route path="/about" element={<About />} />

      <Route element={<AppLayout />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/courses" element={<Courses />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/tutorial" element={<Tutorial />} />
        <Route path="/courses/:id" element={<CourseDetail />} />
        <Route path="/study-goals" element={<StudyGoals />} />
        <Route path="/study-goals/:id" element={<StudyGoalsDetails />} />
      </Route>
    </Routes>
  );
}

export default App;
