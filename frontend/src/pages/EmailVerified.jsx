import { Box, Container, Typography, Paper, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { CheckCircle as CheckCircleIcon } from "@mui/icons-material";

const EmailVerified = () => {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        minHeight: "100vh",
        width: "100%",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background: "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)",
        position: "fixed",
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        overflow: "auto",
      }}
    >
      <Container maxWidth="sm" sx={{ my: 4 }}>
        <Paper
          elevation={3}
          sx={{
            p: 4,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            background: "rgba(255, 255, 255, 0.9)",
            backdropFilter: "blur(10px)",
          }}
        >
          <CheckCircleIcon
            sx={{
              fontSize: 80,
              color: "success.main",
              mb: 2,
            }}
          />
          <Typography
            component="h1"
            variant="h4"
            sx={{
              mb: 2,
              fontWeight: 600,
              color: "primary.main",
            }}
          >
            Email Verified Successfully!
          </Typography>
          <Typography
            variant="body1"
            sx={{ mb: 4, color: "text.secondary", textAlign: "center" }}
          >
            Your email has been verified. You can now log in to your account.
          </Typography>

          <Button
            variant="contained"
            size="large"
            onClick={() => navigate("/login")}
            sx={{
              py: 1.5,
              px: 4,
              fontSize: "1.1rem",
              textTransform: "none",
            }}
          >
            Go to Login
          </Button>
        </Paper>
      </Container>
    </Box>
  );
};

export default EmailVerified;
