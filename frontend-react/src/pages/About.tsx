import '../components/Navbar.css';
import '../components/AppLayout.css';
import logo_inv from '../assets/logo_inv.jpg';
import { Link } from 'react-router-dom';
import './About.css';
import {
  FiCalendar,
  FiUploadCloud,
  FiFileText,
  FiMessageCircle,
  FiCheckSquare,
  FiChevronDown,
  FiServer,
  FiGithub
} from 'react-icons/fi';

const features = [
  {
    icon: <FiCalendar />,
    title: 'Academic calendar',
    text: 'Build your own academic calendar and keep every deadline in one place',
  },
  {
    icon: <FiServer />,
    title: 'Smart input',
    text: 'Add events with smart input, just paste text or upload a photo of your syllabus',
  },
  {
    icon: <FiFileText />,
    title: 'AI summaries',
    text: 'Get AI-generated summaries for each study goal',
  },
  {
    icon: <FiMessageCircle />,
    title: 'AI tutor',
    text: 'Ask your AI tutor anytime, right from the chatbot',
  },
  {
    icon: <FiUploadCloud />,
    title: 'Always updated',
    text: 'Keep your AI tutor updated with your own notes',
  },
  {
    icon: <FiCheckSquare />,
    title: 'Practice tests',
    text: 'Test yourself with auto-generated practice tests before the real exam',
  },
];

const faqs = [
  {
    question: 'Is Study Buddy free to use?',
    answer: 'Yes, all core features calendar, AI summaries, tutor chatbot and practice tests, are free to use.',
  },
  {
    question: 'Is Study Buddy a corporation?',
    answer: 'No, this is a personal portfolio project. Feel free to check out the source code on GitHub.',
  },
  {
    question: 'Is Study Buddy unlimited?',
    answer: 'You can create unlimited events and manage your time, but the tokens for AI use is limited.',
  },
  {
    question: 'How does the smart syllabus input work?',
    answer: 'Just paste the text of your syllabus or upload a photo of it, and Study Buddy will automatically extract the dates and create calendar events for you.',
  },
  {
    question: 'Is my data safe?',
    answer: 'Your account and study materials are private and only accessible to you. We never share your data with third parties.',
  },
  {
    question: 'How does the AI tutor work?',
    answer: 'The AI tutor is a chatbot you can ask questions anytime, it helps explain concepts and clarify doubts related to your study materials.',
  },
];

function About() {

  return (
    <div className="about-page">
      <nav className="navbar">
        <Link to="/dashboard">
          <img src={logo_inv} alt="Study Buddy" className="navbar-logo" />
        </Link>
      </nav>

      <div className="about-content">
        <div className="about-card">
          <h1>About Study Buddy</h1>

          <p>
            Study Buddy was born from a simple need: making studying feel less 
            chaotic. Organizing your academic life shouldn't be complicated,
            that's why this tool brings your calendar, materials, and study tools 
            together, with a little help from AI.
          </p>

          <div className="about-features">
            {features.map((feature) => (
              <div key={feature.title} className="about-feature">
                <div className="about-feature-icon">{feature.icon}</div>
                <div className="about-feature-text">
                  <h3>{feature.title}</h3>
                  <p>{feature.text}</p>
                </div>
              </div>
            ))}
          </div>

          <h2 className="about-faq-title">Frequently asked questions</h2>

          <div className="about-faq">
            {faqs.map((faq) => (
              <details key={faq.question} className="about-faq-item">
                <summary>
                  <span>{faq.question}</span>
                  <FiChevronDown className="about-faq-chevron" />
                </summary>
                <p>{faq.answer}</p>
              </details>
            ))}
          </div>
        </div>
      </div>

      <a
        href="https://github.com/jconstmateus"
        target="_blank"
        rel="noopener noreferrer"
        className="about-github-fab"
        aria-label="View source on GitHub"
      >
        <FiGithub />
      </a>

      <footer className="app-layout-footer">
        <span>© 2026 Study Buddy</span>
      </footer>
    </div>
  );

}

export default About;
