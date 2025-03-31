import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import authService from "../services/authService";
import "../styles/global.css";

const Dashboard = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const currentUser = await authService.getCurrentUser();
        if (!currentUser) {
          navigate("/login");
          return;
        }
        setUser(currentUser);
      } catch (error) {
        navigate("/login");
      }
    };
    checkAuth();
  }, [navigate]);

  const handleLogout = async () => {
    try {
      await authService.logout();
      navigate("/login");
    } catch (error) {
      toast.error("Failed to logout. Please try again.");
    }
  };

  if (!user) {
    return null;
  }

  return (
    <div className="page">
      <nav className="nav">
        <div className="container nav-content">
          <h1 className="heading-lg" style={{ margin: 0 }}>
            Dashboard
          </h1>
          <button className="btn btn-secondary" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </nav>

      <main className="container mt-4">
        <div className="grid grid-2">
          <div className="card">
            <div className="text-center">
              <div className="avatar">
                {user.name ? user.name[0].toUpperCase() : "U"}
              </div>
              <h2 className="heading-lg">{user.name || "User"}</h2>
            </div>
            <div className="divider"></div>
            <div>
              <div
                className="mb-2"
                style={{ display: "flex", alignItems: "center" }}
              >
                <span className="icon">📧</span>
                <span className="text-body" style={{ margin: 0 }}>
                  {user.email}
                </span>
              </div>
              {user.phoneNumber && (
                <div style={{ display: "flex", alignItems: "center" }}>
                  <span className="icon">📱</span>
                  <span className="text-body" style={{ margin: 0 }}>
                    {user.phoneNumber}
                  </span>
                </div>
              )}
            </div>
          </div>

          <div className="card">
            <h2 className="heading-xl">Welcome to Your Dashboard</h2>
            <p className="text-body">
              You are successfully logged in to your account. This is a
              protected route that only authenticated users can access.
            </p>
            <p className="text-body" style={{ marginBottom: 0 }}>
              Here you can manage your profile, view your account details, and
              access other features of the application.
            </p>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
