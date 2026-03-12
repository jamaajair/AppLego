import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
    palette: {
        mode: 'light',
        primary: {
            main: '#1f3a5f',
        },
        secondary: {
            main: '#607d8b',
        },
        background: {
            default: '#f4f6f8',
            paper: '#ffffff',
        },
    },
    shape: {
        borderRadius: 8,
    },
    typography: {
        fontFamily: '"Inter", "Roboto", sans-serif',
        h6: {
            fontWeight: 600,
        },
        button: {
            textTransform: 'none',
            fontWeight: 500,
        },
    },
    components: {
        MuiAppBar: {
            styleOverrides: {
                root: {
                    boxShadow: 'none',
                    borderBottom: '1px solid #e0e0e0',
                },
            },
        },
        MuiButton: {
            defaultProps: {
                disableElevation: true,
            },
        },
    },
});