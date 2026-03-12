import { useEffect, useState } from "react";
import {
    Box,
    Typography,
    Button,
    CircularProgress,
    Card,
    CardHeader,
    CardContent,
    Divider,
    List,
    ListItem,
    ListItemAvatar,
    Avatar,
    ListItemText,
    Chip,
    Stack,
    IconButton,
    Tooltip
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";

import AppError from "../../ui/AppError.tsx";
import useStakeholder from "../../../hooks/useStakeholder.tsx";
import AddStakeholderDialog from "./AddStakeholderDialog.tsx";
import ConfirmDeleteDialog from "../../ui/ConfirmDeleteDialog.tsx"
import {api} from "../../../services/api.ts";

interface DossierStakeholdersProps {
    dossierRef: string;
}

export default function DossierStakeholdersPanel({ dossierRef }: DossierStakeholdersProps) {
    const {
        loading,
        error,
        dossierParties,
        availableParties,
        roles,
        dialogOpen,
        openDialog,
        closeDialog,
        loadDossierParties,
        addStakeholder
    } = useStakeholder(dossierRef);

    const [deleteOpen, setDeleteOpen] = useState(false);
    const [deleteLoading, setDeleteLoading] = useState(false);
    const [selectedToDelete, setSelectedToDelete] = useState<{ ref: number; label: string } | null>(null);
    const [localError, setLocalError] = useState<string | null>(null);

    useEffect(() => {
        loadDossierParties();
    }, [loadDossierParties]);

    const openDelete = (ref: number, label: string) => {
        setSelectedToDelete({ ref, label });
        setLocalError(null);
        setDeleteOpen(true);
    };

    const closeDelete = () => {
        if (deleteLoading) return;
        setDeleteOpen(false);
        setSelectedToDelete(null);
        setLocalError(null);
    };

    const handleDelete = async () => {
        if (!selectedToDelete) return;

        setDeleteLoading(true);
        setLocalError(null);

        try {
            await api.delete(`/user/dossiers/${dossierRef}/stakeholders/${selectedToDelete.ref}`, null);
            setDeleteOpen(false);
            setSelectedToDelete(null);
            await loadDossierParties();
        } catch (err) {
            console.error(err);
            setLocalError("Erreur lors de la suppression.");
        } finally {
            setDeleteLoading(false);
        }
    };

    return (
        <Box sx={{ p: 3, mx: "auto", maxWidth: 920 }}>
            <Card elevation={2} sx={{ borderRadius: 3 }}>
                <CardHeader
                    title={<Typography variant="h6">Parties prenantes</Typography>}
                    subheader={
                        <Typography variant="body2" color="text.secondary">
                            Gérez les parties liées au dossier et leur rôle.
                        </Typography>
                    }
                    action={
                        <Button variant="contained" startIcon={<AddIcon />} onClick={openDialog} disabled={loading}>
                            Ajouter
                        </Button>
                    }
                    sx={{ pb: 1.5 }}
                />
                <Divider />

                <CardContent sx={{ pt: 2 }}>
                    {(error || localError) && (
                        <AppError message={error ?? localError ?? "Erreur"} onRetry={loadDossierParties} centered />
                    )}

                    {loading ? (
                        <Box sx={{ display: "flex", justifyContent: "center", py: 5 }}>
                            <CircularProgress />
                        </Box>
                    ) : dossierParties.length === 0 ? (
                        <Box sx={{ py: 4 }}>
                            <Typography sx={{ fontWeight: 600 }}>Aucune partie prenante</Typography>
                            <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
                                Ajoute une première partie pour commencer.
                            </Typography>
                        </Box>
                    ) : (
                        <List disablePadding>
                            {dossierParties.map((p) => (
                                <ListItem
                                    key={p.ref}
                                    divider
                                    secondaryAction={
                                        <Tooltip title="Supprimer">
                                            <IconButton edge="end" onClick={() => openDelete(p.ref, p.label)}>
                                                <DeleteOutlineIcon />
                                            </IconButton>
                                        </Tooltip>
                                    }
                                    sx={{
                                        px: 0,
                                        py: 1.25,
                                        "&:hover": { backgroundColor: "action.hover" },
                                        borderRadius: 2
                                    }}
                                >
                                    <ListItemAvatar sx={{ minWidth: 44 }}>
                                        <Avatar sx={{ width: 32, height: 32 }}>
                                            {String(p.label ?? "?").trim().slice(0, 1).toUpperCase()}
                                        </Avatar>
                                    </ListItemAvatar>

                                    <ListItemText
                                        primary={
                                            <Stack direction="row" alignItems="center" spacing={1} sx={{ minWidth: 0 }}>
                                                <Typography sx={{ fontWeight: 700 }} noWrap>
                                                    {p.label}
                                                </Typography>
                                                {p.role?.labelFr ? (
                                                    <Chip size="small" label={p.role.labelFr} variant="outlined" />
                                                ) : (
                                                    <Chip size="small" label="Rôle —" variant="outlined" />
                                                )}
                                            </Stack>
                                        }
                                    />
                                </ListItem>
                            ))}
                        </List>
                    )}
                </CardContent>
            </Card>

            <AddStakeholderDialog
                open={dialogOpen}
                onClose={closeDialog}
                parties={availableParties}
                existingParties={dossierParties}
                roles={roles}
                onSubmit={(partyRef, role) => addStakeholder(partyRef, role)}
            />

            <ConfirmDeleteDialog
                open={deleteOpen}
                loading={deleteLoading}
                onCancel={closeDelete}
                onConfirm={handleDelete}
                message={
                    selectedToDelete
                        ? `Supprimer la partie prenante “${selectedToDelete.label}” de ce dossier ?`
                        : "Supprimer cette partie prenante ?"
                }
            />
        </Box>
    );
}
