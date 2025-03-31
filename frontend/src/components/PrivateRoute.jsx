import { Navigate } from "react-router-dom";
import authService from "../services/authService";

const PrivateRoute = ({ children }) => {
  const user = authService.getCurrentUser();

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default PrivateRoute;
