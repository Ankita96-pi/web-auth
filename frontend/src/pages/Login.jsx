import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useFormik } from "formik";
import * as Yup from "yup";
import { toast } from "react-toastify";
import authService from "../services/authService";
import "../styles/global.css";

const validationSchema = Yup.object({
  email: Yup.string()
    .email("Invalid email address")
    .required("Email is required"),
  password: Yup.string().required("Password is required"),
});

const Login = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const formik = useFormik({
    initialValues: {
      email: "",
      password: "",
    },
    validationSchema: validationSchema,
    onSubmit: async (values) => {
      try {
        setLoading(true);
        const response = await authService.login(values.email, values.password);
        toast.success("Login successful!");
        navigate("/dashboard");
      } catch (error) {
        toast.error(error.error || "Failed to login. Please try again.");
      } finally {
        setLoading(false);
      }
    },
  });

  return (
    <div className="page">
      <div className="container" style={{ maxWidth: "400px" }}>
        <div className="card mt-4">
          <h1 className="heading-xl text-center">Welcome Back</h1>
          <p className="text-body text-center">
            Please sign in to your account
          </p>

          <form onSubmit={formik.handleSubmit}>
            <div className="form-group">
              <label htmlFor="email" className="form-label">
                Email Address
              </label>
              <input
                id="email"
                name="email"
                type="email"
                className="form-input"
                placeholder="Enter your email"
                {...formik.getFieldProps("email")}
              />
              {formik.touched.email && formik.errors.email && (
                <div className="form-error">{formik.errors.email}</div>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="password" className="form-label">
                Password
              </label>
              <input
                id="password"
                name="password"
                type="password"
                className="form-input"
                placeholder="Enter your password"
                {...formik.getFieldProps("password")}
              />
              {formik.touched.password && formik.errors.password && (
                <div className="form-error">{formik.errors.password}</div>
              )}
            </div>

            <button
              type="submit"
              className="btn btn-primary"
              style={{ width: "100%" }}
              disabled={loading}
            >
              {loading ? <div className="spinner"></div> : "Sign In"}
            </button>

            <div className="mt-3 text-center">
              <Link to="/forgot-password" className="link">
                Forgot password?
              </Link>
            </div>

            <div className="mt-2 text-center">
              <span className="text-body" style={{ marginRight: "8px" }}>
                Don't have an account?
              </span>
              <Link to="/register" className="link">
                Sign Up
              </Link>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;
