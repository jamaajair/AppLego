import { useEffect } from 'react';
import {
  Button, CircularProgress, Typography, Box,
  Paper, List, ListItem, ListItemText
} from '@mui/material';

import useStakeholder from '../hooks/useStakeholder';
import AddStakeholderDialog from './AddStakeholderDialog';

interface DossierStakeholdersProps {
    dossierRef: string;
}

export default function AddStakeholder({ dossierRef }: DossierStakeholdersProps) {

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

    useEffect(() => {
    loadDossierParties();
    }, []);

    return (
        <Box sx={{
            p: 3,
            mx: 'auto',
            maxWidth: 800
            }}>

            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography variant="h5">Parties Prenantes</Typography>
                <Button variant="contained" onClick={openDialog}>Ajouter une partie</Button>
            </Box>

            {error && <Typography color="error">{error}</Typography>}

            {loading ? (
                <CircularProgress />
                ) : (
                <Paper elevation={2}>
                    <List>
                        {dossierParties.length === 0 ? (
                            <ListItem>
                                <Typography>Aucune partie prenante pour ce dossier.</Typography>
                            </ListItem>
                            ) : (
                                dossierParties.map((p) => (
                                <ListItem key={p.ref} divider>
                                <ListItemText primary={p.label} />
                                <ListItemText secondary={p.kind} />
                                <ListItemText secondary={`Rôle: ${p.role?.labelFr}`} />
                                </ListItem>
                            ))
                        )}
                    </List>
                </Paper>
            )}

            <AddStakeholderDialog
                open={dialogOpen}
                onClose={closeDialog}
                parties={availableParties}
                roles={roles}
                onSubmit={(partyRef, role) => addStakeholder(partyRef, role)}
            />
        </Box>
    );
}
