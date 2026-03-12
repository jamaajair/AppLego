import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Typography } from "@mui/material";

interface ConfirmDeleteDialogProps {
    open: boolean;
    title?: string;
    message: string;
    confirmLabel?: string;
    cancelLabel?: string;
    loading?: boolean;
    onCancel: () => void;
    onConfirm: () => void;
}

export default function ConfirmDeleteDialog({
                                                open,
                                                title = "Confirmer la suppression",
                                                message,
                                                confirmLabel = "Supprimer",
                                                cancelLabel = "Annuler",
                                                loading = false,
                                                onCancel,
                                                onConfirm
                                            }: ConfirmDeleteDialogProps) {
    return (
        <Dialog open={open} onClose={loading ? undefined : onCancel} maxWidth="xs" fullWidth>
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>
                <Typography variant="body2" color="text.secondary">
                    {message}
                </Typography>
            </DialogContent>
            <DialogActions sx={{ px: 3, pb: 2 }}>
                <Button onClick={onCancel} disabled={loading} variant="outlined">
                    {cancelLabel}
                </Button>
                <Button onClick={onConfirm} disabled={loading} color="error" variant="contained">
                    {confirmLabel}
                </Button>
            </DialogActions>
        </Dialog>
    );
}