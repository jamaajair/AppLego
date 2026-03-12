import { Link as RouterLink } from "react-router-dom";
import {
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    Divider,
    Stack,
    Typography,
} from "@mui/material";
import PageShell from "../components/layout/PageShell";
import { useAuth } from "../hooks/useAuth.tsx";

function StatCard({
                      label,
                      value,
                      hint,
                  }: {
    label: string;
    value: string;
    hint?: string;
}) {
    return (
        <Card sx={{ height: "100%" }}>
            <CardContent>
                <Typography variant="overline" color="text.secondary">
                    {label}
                </Typography>
                <Typography variant="h4" fontWeight={800} sx={{ mt: 0.5 }}>
                    {value}
                </Typography>
                {hint && (
                    <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                        {hint}
                    </Typography>
                )}
            </CardContent>
        </Card>
    );
}

function QuickAction({
                         title,
                         description,
                         to,
                         cta,
                         variant = "contained",
                     }: {
    title: string;
    description: string;
    to: string;
    cta: string;
    variant?: "contained" | "outlined" | "text";
}) {
    return (
        <Card sx={{ height: "100%" }}>
            <CardContent>
                <Typography variant="h6" fontWeight={750}>
                    {title}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                    {description}
                </Typography>

                <Button
                    component={RouterLink}
                    to={to}
                    variant={variant}
                    sx={{ mt: 2 }}
                >
                    {cta}
                </Button>
            </CardContent>
        </Card>
    );
}

export default function Home() {
    const { userId, groups } = useAuth();
    const isAdmin = groups.includes("ADMIN");

    // Pour l’instant ce sont des placeholders.
    // Tu pourras remplacer par de vraies stats via API plus tard.
    const stats = {
        dossiers: "—",
        stakeholders: "—",
        role: groups[0] ?? "—",
    };

    return (
        <PageShell
            title="Welcome"
            action={
                <Stack direction="row" spacing={1} alignItems="center">
                    {groups.map((g) => (
                        <Chip key={g} label={g} size="small" />
                    ))}
                </Stack>
            }
        >
            <Stack spacing={3}>
                {/* HERO */}
                <Card sx={{ overflow: "hidden" }}>
                    <CardContent>
                        <Typography variant="h4" fontWeight={900} sx={{ lineHeight: 1.15 }}>
                            Hello{userId ? `, ${userId}` : ""}.
                        </Typography>
                        <Typography variant="body1" color="text.secondary" sx={{ mt: 1 }}>
                            Manage dossiers, stakeholders and workflows in one place — with
                            role-based access control and a clean navigation structure.
                        </Typography>

                        <Divider sx={{ my: 2 }} />

                        <Stack direction={{ xs: "column", sm: "row" }} spacing={1.5}>
                            <Button
                                variant="contained"
                                component={RouterLink}
                                to="/user/dossiers"
                            >
                                Open dossiers
                            </Button>

                            <Button
                                variant="outlined"
                                component={RouterLink}
                                to="/user/stakeholders"
                            >
                                Browse stakeholders
                            </Button>

                            {isAdmin && (
                                <Button
                                    variant="outlined"
                                    component={RouterLink}
                                    to="/admin"
                                >
                                    Admin dashboard
                                </Button>
                            )}
                        </Stack>
                    </CardContent>

                    {/* Bandeau discret en bas */}
                    <Box
                        sx={{
                            px: 2.5,
                            py: 1.5,
                            bgcolor: "rgba(0,0,0,0.04)",
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                            gap: 2,
                            flexWrap: "wrap",
                        }}
                    >
                        <Typography variant="body2" color="text.secondary">
                            Session: authenticated
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Environment: local
                        </Typography>
                    </Box>
                </Card>

                {/* STATS */}
                <Box
                    sx={{
                        display: "grid",
                        gridTemplateColumns: { xs: "1fr", md: "1fr 1fr 1fr" },
                        gap: 2,
                    }}
                >
                    <StatCard
                        label="Dossiers"
                        value={stats.dossiers}
                        hint="Total visible dossiers (placeholder)"
                    />
                    <StatCard
                        label="Stakeholders"
                        value={stats.stakeholders}
                        hint="Total available stakeholders (placeholder)"
                    />
                    <StatCard
                        label="Primary role"
                        value={stats.role}
                        hint="Derived from JWT groups claim"
                    />
                </Box>

                {/* QUICK ACTIONS */}
                <Box
                    sx={{
                        display: "grid",
                        gridTemplateColumns: { xs: "1fr", md: "1fr 1fr" },
                        gap: 2,
                    }}
                >
                    <QuickAction
                        title="Create a new dossier"
                        description="Start a new dossier and fill in the required metadata."
                        to="/user/dossiers/new"
                        cta="New dossier"
                        variant="contained"
                    />

                    <QuickAction
                        title="Review my dossiers"
                        description="Open the dossiers list and continue where you left off."
                        to="/user/dossiers"
                        cta="Go to list"
                        variant="outlined"
                    />
                </Box>

                {/* RECENT / ACTIVITY PLACEHOLDER */}
                <Card>
                    <CardContent>
                        <Typography variant="h6" fontWeight={800} gutterBottom>
                            Recent activity
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            This section can display recent dossier updates, assignments or
                            notifications. (Placeholder for now.)
                        </Typography>
                    </CardContent>
                </Card>
            </Stack>
        </PageShell>
    );
}
