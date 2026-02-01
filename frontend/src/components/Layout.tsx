import React, {useState, useEffect, type ReactNode, type FC} from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Tooltip from '@mui/material/Tooltip';
import IconButton from '@mui/material/IconButton';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import { Outlet } from 'react-router-dom';


const user_pages = {'Accueil': '/user/',
                    'Mes dossiers': '/user/dossiers/',
                    'Notifications':'/user/notifications',
                    'Support':'/support'};

const settings = {'Profile': '/user/profile', 'Account':'/user/account', 'Dashboard':'/dashboard', 'Logout':'/logout'};

const Layout: FC<LayoutProps> = () => {
    const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null);
    const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);

    const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElNav(event.currentTarget);
    };
    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    return ((
            <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
            {/* Header */}
                <AppBar position="fixed" sx={{ width:'100%'}} style={{ backgroundColor: '#1976d2' }}>
                    <Toolbar disableGutters>
                        <Typography variant="h6" component="div" fontSize="1.6rem">SPFTech@ULG</Typography>

                        <Box sx={{display: 'flex', flexDirection: 'row', flexGrow:'0.5', justifyContent: 'center', }}>
                        {Object.entries(user_pages).map(([page, link]) => (
                            <Button
                                key={page}
                                href={link}
                                sx={{ color: 'white', textTransform: 'none', mx: 1, fontSize:'1.2rem'}}
                            >
                                {page}
                            </Button>
                        ))} </Box>


                        <Box sx={{ display:'flex',  alignItems:'center', ml: 'auto'}}>
                            <Tooltip title="Open settings">
                                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                                    <Avatar alt="Remy Sharp" src="/static/images/avatar/2.jpg" />
                                </IconButton>
                            </Tooltip>
                            <Menu
                                id="menu-appbar"
                                anchorEl={anchorElUser}
                                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                                keepMounted
                                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                                open={Boolean(anchorElUser)}
                                onClose={handleCloseUserMenu}
                            >
                                {Object.entries(settings).map(([page, link]) => (
                                    <MenuItem key={page} href={link} sx={{ my: 2, color: 'black', display: 'block' }}>
                                        {page}
                                    </MenuItem>
                                ))}
                            </Menu>
                        </Box>
                    </Toolbar>
                </AppBar>

            {/* Main Page */}
            <Box component="main" sx={{flexGrow: 1, mt:2, p:3}}>
                <Outlet/>
            </Box>

            {/* Footer */}
            <Box
                component="footer"
                sx={{
                    position: 'fixed',
                    bottom: 0,
                    left: 0,
                    right: 0,
                    width: '100%',
                    backgroundColor: '#1976d2',
                    color: 'white',
                    textAlign: 'center',
                    py: 2,
                }}
            >
                <Typography variant="body2">
                    © 2025-26 SPFTech@ULG. Tous droits réservés.
                </Typography>
            </Box>

        </Box>
        )
    )
}

export default Layout;