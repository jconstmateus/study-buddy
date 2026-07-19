import type { ReactNode } from 'react';
import { Navigate } from 'react-router-dom';


function ProtectedRoute({ children }: { children: ReactNode }) {
  
  // Get token from storage
  const token = localStorage.getItem("token");

  if (!token) { // Go back to root (authentication)
    return <Navigate to="/" replace />;
  }

  // Else return the interface 
  return children;
}

export default ProtectedRoute;
