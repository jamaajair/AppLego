import { Alert, Snackbar } from "@mui/material";

type AppErrorPopupProps = {
    message: string | null;
    onClose: () => void;
};

export default function AppErrorPopup({ message, onClose }: AppErrorPopupProps) {
    return (
        <Snackbar
            open={!!message}
            autoHideDuration={5000}
            onClose={onClose}
            anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        >
            <Alert severity="error" onClose={onClose}>
                {message}
            </Alert>
        </Snackbar>
    );
}