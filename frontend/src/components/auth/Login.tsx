import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
    Box,
    Button,
    Card,
    CardContent,
    TextField,
    Typography,
    Alert,
} from "@mui/material";
import { useAuth } from "../../hooks/useAuth.tsx";

export default function LoginPage() {
    const { login } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const [username, setUsername] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    // Redirection après login
    const from = (location.state)?.from?.pathname || "/user";

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            await login(username);
            navigate(from, { replace: true });
        } catch (err: any) {
            setError("Invalid login");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            height="100vh"
            bgcolor="#f4f6f8"
        >
            <Card sx={{ width: 400, p: 2 }}>
                <CardContent>
                    <Typography variant="h5" gutterBottom>
                        Login
                    </Typography>

                    {error && <Alert severity="error">{error}</Alert>}

                    <form onSubmit={handleSubmit}>
                        <TextField
                            label="Username"
                            fullWidth
                            margin="normal"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />

                        <Button
                            type="submit"
                            variant="contained"
                            fullWidth
                            disabled={loading}
                            sx={{ mt: 2 }}
                        >
                            {loading ? "Signing in..." : "Login"}
                        </Button>
                    </form>

                    <Typography variant="body2" sx={{ mt: 2 }}>
                        Try: <b>admin</b>, <b>supervisor</b>, <b>bob</b>
                    </Typography>
                </CardContent>
            </Card>
        </Box>
    );
}
