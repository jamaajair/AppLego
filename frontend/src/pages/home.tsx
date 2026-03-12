
import {
    Box,
    Container,
    Typography,
    Button,
    Card,
    CardContent,
    CardActions,
    Chip,
    Stack,
    Divider,
    Paper,
    TextField,
    InputAdornment,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Grid
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import VerifiedUserIcon from "@mui/icons-material/VerifiedUser";
import GavelIcon from "@mui/icons-material/Gavel";
import AccountBalanceIcon from "@mui/icons-material/AccountBalance";
import InsightsIcon from "@mui/icons-material/Insights";
import DescriptionIcon from "@mui/icons-material/Description";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import CheckCircleOutlineIcon from "@mui/icons-material/CheckCircleOutline";

export default function HomePage() {
    return (
        <Box sx={{ minHeight: "100vh", bgcolor: "background.default" }}>
            {/* Hero */}
            <Box
                sx={{
                    bgcolor: "background.paper",
                    borderBottom: (t) => `1px solid ${t.palette.divider}`,
                }}
            >
                <Container maxWidth="lg" sx={{ py: { xs: 6, md: 9 } }}>
                    <Grid container spacing={4} alignItems="center">
                        <Grid container xs={12} md={7}>
                            <Stack spacing={2}>
                                <Stack direction="row" spacing={1} flexWrap="wrap">
                                    <Chip size="small" label="POC" />
                                    <Chip size="small" label="SPF Finances" />
                                    <Chip size="small" label="Agence gestion du patrimoine" />
                                </Stack>

                                <Typography variant="h3" sx={{ fontWeight: 800, letterSpacing: -0.5 }}>
                                    Gestion du patrimoine public, en un point d’accès.
                                </Typography>

                                <Typography variant="h6" color="text.secondary" sx={{ lineHeight: 1.5 }}>
                                    Accédez rapidement aux services, informations et documents liés à la gestion
                                    et à la valorisation du patrimoine de l’État.
                                </Typography>

                                <Stack direction={{ xs: "column", sm: "row" }} spacing={1.5} sx={{ pt: 1 }}>
                                    <Button variant="contained" size="large" endIcon={<ArrowForwardIcon />}>
                                        Accéder aux services
                                    </Button>
                                    <Button variant="outlined" size="large">
                                        Consulter la documentation
                                    </Button>
                                </Stack>

                                <Paper
                                    variant="outlined"
                                    sx={{
                                        mt: 2,
                                        p: 1.5,
                                        borderRadius: 3,
                                        display: "flex",
                                        gap: 1,
                                        alignItems: "center",
                                    }}
                                >
                                    <TextField
                                        fullWidth
                                        placeholder="Rechercher un dossier, un bâtiment, un document…"
                                        size="small"
                                        InputProps={{
                                            startAdornment: (
                                                <InputAdornment position="start">
                                                    <SearchIcon fontSize="small" />
                                                </InputAdornment>
                                            ),
                                        }}
                                    />
                                    <Button variant="contained" sx={{ whiteSpace: "nowrap" }}>
                                        Rechercher
                                    </Button>
                                </Paper>

                                <Typography variant="body2" color="text.secondary">
                                    Exemple POC — pas de données réelles, contenus indicatifs.
                                </Typography>
                            </Stack>
                        </Grid>

                        <Grid container xs={12} md={5}>
                            <Paper
                                variant="outlined"
                                sx={{
                                    p: 3,
                                    borderRadius: 4,
                                    bgcolor: "background.default",
                                }}
                            >
                                <Stack spacing={2}>
                                    <Typography variant="subtitle1" sx={{ fontWeight: 700 }}>
                                        Accès rapide
                                    </Typography>

                                    <Grid container spacing={1.5}>
                                        {quickActions.map((a) => (
                                            <Grid container key={a.title} item xs={12} sm={6} md={12}>
                                                <Card variant="outlined" sx={{ borderRadius: 3 }}>
                                                    <CardContent sx={{ pb: 1 }}>
                                                        <Stack direction="row" spacing={1.5} alignItems="center">
                                                            <Box sx={{ color: "text.secondary" }}>{a.icon}</Box>
                                                            <Box>
                                                                <Typography sx={{ fontWeight: 700 }}>{a.title}</Typography>
                                                                <Typography variant="body2" color="text.secondary">
                                                                    {a.desc}
                                                                </Typography>
                                                            </Box>
                                                        </Stack>
                                                    </CardContent>
                                                    <CardActions sx={{ px: 2, pb: 2, pt: 0 }}>
                                                        <Button size="small" endIcon={<ArrowForwardIcon />}>
                                                            Ouvrir
                                                        </Button>
                                                    </CardActions>
                                                </Card>
                                            </Grid>
                                        ))}
                                    </Grid>

                                    <Divider />

                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <InfoOutlinedIcon fontSize="small" color="action" />
                                        <Typography variant="body2" color="text.secondary">
                                            Connexion eID / itsme® (placeholder POC).
                                        </Typography>
                                    </Stack>
                                </Stack>
                            </Paper>
                        </Grid>
                    </Grid>
                </Container>
            </Box>

            {/* Services */}
            <Container maxWidth="lg" sx={{ py: { xs: 6, md: 7 } }}>
                <Stack spacing={1} sx={{ mb: 3 }}>
                    <Typography variant="h5" sx={{ fontWeight: 800 }}>
                        Services principaux
                    </Typography>
                    <Typography color="text.secondary">
                        Les parcours les plus fréquents pour la gestion du patrimoine.
                    </Typography>
                </Stack>

                <Grid container spacing={2.5}>
                    {services.map((s) => (
                        <Grid key={s.title} item xs={12} md={4}>
                            <Card
                                variant="outlined"
                                sx={{
                                    height: "100%",
                                    borderRadius: 4,
                                    transition: "transform 140ms ease",
                                    "&:hover": { transform: "translateY(-2px)" },
                                }}
                            >
                                <CardContent>
                                    <Stack spacing={1.5}>
                                        <Box sx={{ color: "text.secondary" }}>{s.icon}</Box>
                                        <Typography variant="h6" sx={{ fontWeight: 800 }}>
                                            {s.title}
                                        </Typography>
                                        <Typography color="text.secondary">{s.desc}</Typography>
                                        <Stack direction="row" spacing={1} flexWrap="wrap">
                                            {s.tags.map((t) => (
                                                <Chip key={t} size="small" label={t} variant="outlined" />
                                            ))}
                                        </Stack>
                                    </Stack>
                                </CardContent>
                                <CardActions sx={{ px: 2, pb: 2 }}>
                                    <Button endIcon={<ArrowForwardIcon />}>Démarrer</Button>
                                </CardActions>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            </Container>

            {/* Trust / compliance */}
            <Box sx={{ bgcolor: "background.paper", borderTop: (t) => `1px solid ${t.palette.divider}` }}>
                <Container maxWidth="lg" sx={{ py: { xs: 5, md: 6 } }}>
                    <Grid container spacing={3}>
                        <Grid item xs={12} md={5}>
                            <Typography variant="h5" sx={{ fontWeight: 800, mb: 1 }}>
                                Fiabilité & conformité
                            </Typography>
                            <Typography color="text.secondary" sx={{ mb: 2 }}>
                                Indicateurs POC (à adapter) pour rassurer sur le cadre légal, la traçabilité et
                                la protection des données.
                            </Typography>

                            <List dense>
                                {trustBullets.map((b) => (
                                    <ListItem key={b}>
                                        <ListItemIcon sx={{ minWidth: 34 }}>
                                            <CheckCircleOutlineIcon fontSize="small" />
                                        </ListItemIcon>
                                        <ListItemText primary={b} />
                                    </ListItem>
                                ))}
                            </List>
                        </Grid>

                        <Grid item xs={12} md={7}>
                            <Grid container spacing={2}>
                                {kpis.map((k) => (
                                    <Grid key={k.label} item xs={12} sm={6}>
                                        <Paper
                                            variant="outlined"
                                            sx={{
                                                p: 2.5,
                                                borderRadius: 4,
                                                height: "100%",
                                                bgcolor: "background.default",
                                            }}
                                        >
                                            <Typography variant="overline" color="text.secondary">
                                                {k.label}
                                            </Typography>
                                            <Typography variant="h4" sx={{ fontWeight: 900, mt: 0.5 }}>
                                                {k.value}
                                            </Typography>
                                            <Typography color="text.secondary" sx={{ mt: 0.5 }}>
                                                {k.hint}
                                            </Typography>
                                        </Paper>
                                    </Grid>
                                ))}
                            </Grid>
                        </Grid>
                    </Grid>
                </Container>
            </Box>

            {/* CTA */}
            <Container maxWidth="lg" sx={{ py: { xs: 6, md: 7 } }}>
                <Paper
                    sx={{
                        p: { xs: 3, md: 4 },
                        borderRadius: 5,
                        bgcolor: "background.paper",
                        border: (t) => `1px solid ${t.palette.divider}`,
                    }}
                >
                    <Grid container spacing={3} alignItems="center">
                        <Grid item xs={12} md={8}>
                            <Typography variant="h5" sx={{ fontWeight: 900, mb: 1 }}>
                                Besoin d’un parcours spécifique ?
                            </Typography>
                            <Typography color="text.secondary">
                                Ajoute ici un lien vers un formulaire, une page “Contact / Support”, ou un
                                portail interne selon ton POC.
                            </Typography>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Stack direction={{ xs: "column", sm: "row", md: "column" }} spacing={1.5}>
                                <Button variant="contained" size="large" endIcon={<ArrowForwardIcon />}>
                                    Demander une assistance
                                </Button>
                                <Button variant="outlined" size="large">
                                    Voir les guides
                                </Button>
                            </Stack>
                        </Grid>
                    </Grid>
                </Paper>
            </Container>

            {/* Footer */}
            <Box sx={{ py: 4, borderTop: (t) => `1px solid ${t.palette.divider}` }}>
                <Container maxWidth="lg">
                    <Grid container spacing={2}>
                        <Grid item xs={12} md={8}>
                            <Typography variant="body2" color="text.secondary">
                                © {new Date().getFullYear()} SPF Finances — Agence gestion du patrimoine (POC)
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                Mentions légales · Accessibilité · Politique de confidentialité (placeholders)
                            </Typography>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Stack direction="row" spacing={1} justifyContent={{ xs: "flex-start", md: "flex-end" }}>
                                <Chip size="small" variant="outlined" label="v0.1" />
                                <Chip size="small" variant="outlined" label="MUI" />
                                <Chip size="small" variant="outlined" label="React" />
                            </Stack>
                        </Grid>
                    </Grid>
                </Container>
            </Box>
        </Box>
    );
}

const quickActions = [
    {
        title: "Dossiers immobiliers",
        desc: "Accéder aux fiches et états.",
        icon: <AccountBalanceIcon />,
    },
    {
        title: "Marchés & contrats",
        desc: "Suivi des documents clés.",
        icon: <DescriptionIcon />,
    },
];

const services = [
    {
        title: "Inventaire du patrimoine",
        desc: "Consulter et enrichir l’inventaire (bâtiments, parcelles, affectations).",
        tags: ["Recherche", "Fiches", "Historique"],
        icon: <InsightsIcon />,
    },
    {
        title: "Conformité & cadre légal",
        desc: "Références, procédures et obligations (placeholders à adapter).",
        tags: ["Légal", "Procédures", "Audit"],
        icon: <GavelIcon />,
    },
    {
        title: "Accès sécurisé",
        desc: "Authentification et traçabilité des accès (eID/itsme® ou interne).",
        tags: ["SSO", "Traçabilité", "Rôles"],
        icon: <VerifiedUserIcon />,
    },
];

const trustBullets = [
    "Traçabilité des actions (journalisation)",
    "Gestion des rôles et habilitations",
    "Protection des données & minimisation",
    "Référentiel documentaire versionné",
];

const kpis = [
    { label: "Dossiers suivis", value: "1 248", hint: "Exemple (données fictives)" },
    { label: "Documents", value: "9 532", hint: "Contrats, PV, plans, annexes…" },
    { label: "Accès journalisés", value: "100%", hint: "Toutes actions tracées (POC)" },
    { label: "Disponibilité", value: "99.9%", hint: "Objectif indicatif" },
];

