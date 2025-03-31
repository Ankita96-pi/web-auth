import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import {
  Container,
  Box,
  Typography,
  Paper,
  CircularProgress,
  Button,
} from "@mui/material";
import { toast } from "react-toastify";
import authService from "../services/authService";

const VerifyEmail = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [verified, setVerified] = useState(false);

  useEffect(() => {
    const verifyEmail = async () => {
      const token = searchParams.get("token");

      if (!token) {
        toast.error("Invalid verification link");
        navigate("/login");
        return;
      }

      try {
        await authService.verifyEmail(token);
        setVerified(true);
        toast.success("Email verified successfully!");
        setTimeout(() => {
          navigate("/email-verified");
        }, 2000);
      } catch (error) {
        toast.error(error.error || "Email verification failed");
        setTimeout(() => {
          navigate("/login");
        }, 2000);
      } finally {
        setLoading(false);
      }
    };

    verifyEmail();
  }, [searchParams, navigate]);

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Paper
          elevation={3}
          sx={{
            padding: 4,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            width: "100%",
          }}
        >
          {loading ? (
            <>
              <CircularProgress />
              <Typography component="h1" variant="h6" sx={{ mt: 2 }}>
                Verifying your email...
              </Typography>
            </>
          ) : verified ? (
            <>
              <Typography component="h1" variant="h5" color="primary">
                Email Verified!
              </Typography>
              <Typography variant="body1" sx={{ mt: 2, textAlign: "center" }}>
                Redirecting you to the success page...
              </Typography>
            </>
          ) : (
            <>
              <Typography component="h1" variant="h5" color="error">
                Verification Failed
              </Typography>
              <Typography variant="body1" sx={{ mt: 2, textAlign: "center" }}>
                The verification link is invalid or has expired. You will be
                redirected to the login page.
              </Typography>
              <Button
                variant="contained"
                sx={{ mt: 3 }}
                onClick={() => navigate("/login")}
              >
                Go to Login
              </Button>
            </>
          )}
        </Paper>
      </Box>
    </Container>
  );
};

export default VerifyEmail;
