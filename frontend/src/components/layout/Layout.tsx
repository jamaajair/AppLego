import React, {useState, type FC, type ReactNode} from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import Tooltip from "@mui/material/Tooltip";
import IconButton from "@mui/material/IconButton";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import { Outlet, useNavigate, Link as RouterLink } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth.tsx";

const user_pages: Record<string, string> = {
    "Accueil": "/user/home",
    "Mes dossiers": "/user/dossiers/",
    "Notifications": "/user/notifications",
    "Support": "/support",
};

const settings: Record<string, string> = {
    Profile: "/user/profile",
    Account: "/user/account",
    Logout: "/logout",
};

interface LayoutProps {
    children?: ReactNode;
}

const Layout: FC<LayoutProps> = () => {
    const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);

    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        handleCloseUserMenu();
        logout();
        navigate("/login", { replace: true });
    };

    return (
        <Box sx={{ minHeight: "100vh", display: "flex", flexDirection: "column" }}>
            {/* HEADER */}
            <AppBar position="sticky" color="primary">
                <Toolbar sx={{ px: 4, gap: 2 }}>
                    <Typography variant="h6" sx={{ fontWeight: 600, whiteSpace: "nowrap" }}>
                        SPFTech — Agence Gestion du Patrimoine
                    </Typography>

                    <Box sx={{ flexGrow: 1 }} />

                    <Box sx={{ display: "flex", alignItems: "center" }}>
                        {Object.entries(user_pages).map(([page, link]) => (
                            <Button
                                key={page}
                                component={RouterLink}
                                to={link}
                                sx={{ mx: 0.5, color: "common.white" }}
                            >
                                {page}
                            </Button>
                        ))}
                    </Box>

                    <Tooltip title="Compte">
                        <IconButton onClick={handleOpenUserMenu} sx={{ ml: 1 }}>
                            <Avatar />
                        </IconButton>
                    </Tooltip>

                    <Menu
                        id="menu-appbar"
                        anchorEl={anchorElUser}
                        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
                        transformOrigin={{ vertical: "top", horizontal: "right" }}
                        open={Boolean(anchorElUser)}
                        onClose={handleCloseUserMenu}
                    >
                        {Object.entries(settings).map(([page, link]) => {
                            if (page === "Logout") {
                                return (
                                    <MenuItem key={page} onClick={handleLogout}>
                                        Logout
                                    </MenuItem>
                                );
                            }

                            return (
                                <MenuItem
                                    key={page}
                                    component={RouterLink}
                                    to={link}
                                    onClick={handleCloseUserMenu}
                                >
                                    {page}
                                </MenuItem>
                            );
                        })}
                    </Menu>
                </Toolbar>
            </AppBar>

            {/* MAIN */}
            <Box
                component="main"
                sx={{
                    flex: "1 0 auto",
                    px: 0,
                    py: 0,
                    width: "100%",
                }}
            >
                <Outlet />
            </Box>

            {/* FOOTER */}
            <Box
                component="footer"
                sx={{
                    flexShrink: 0,
                    py: 2,
                    textAlign: "center",
                    bgcolor: "primary.main",
                    color: "common.white",
                }}
            >
                <Typography variant="body2">
                    © 2025 SPF Finances — Agence gestion du patrimoine
                </Typography>
            </Box>
        </Box>
    );
};

export default Layout;