import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';

import type { Dossier } from '../types/dossier';

interface EditDossierDialogProps {
  open: boolean;
  onClose: () => void;
  onSave: (updatedDossier: Partial<Dossier>) => void;
  title: string;
  dossier: Dossier;
}

export default function EditDossierDialog({open, onClose, onSave, title, dossier}: EditDossierDialogProps)
{
    const [localLabel, setLocalLabel] = React.useState(dossier.label);
    const [localStartNoLaterThan, setLocalStartNoLaterThan] = React.useState(dossier.startNoLaterThan);
    const [localExpectCompletion, setLocalExpectCompletion] = React.useState(dossier.expectCompletion);
    const [localState, setLocalState] = React.useState(dossier.state);
    const [localType, setLocalType] = React.useState(dossier.type);

    React.useEffect(() => {
        setLocalLabel(dossier.label);
        setLocalStartNoLaterThan(dossier.startNoLaterThan);
        setLocalExpectCompletion(dossier.expectCompletion);
        setLocalState(dossier.state);
        setLocalType(dossier.type);
    }, [dossier]);

    const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    onSave({
        label: localLabel,
        expectCompletion: localExpectCompletion,
        startNoLaterThan: localStartNoLaterThan,
        state: localState,
        type: localType,
    });
    onClose();
    };
    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle> {title} </DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="label"
                        name="label"
                        label="Label du dossier"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={localLabel}
                        onChange={(e) => setLocalLabel(e.target.value)}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="startNoLaterThan"
                        name="startNoLaterThan"
                        label="Date de début"
                        type="date"
                        fullWidth
                        variant="outlined"
                        value={localStartNoLaterThan ? String(localStartNoLaterThan).slice(0,10): localStartNoLaterThan}
                        onChange={(e) => setLocalStartNoLaterThan(e.target.value)}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="expectCompletion"
                        name="expectCompletion"
                        label="Date de fin"
                        type="date"
                        fullWidth
                        variant="outlined"
                        value={localExpectCompletion ? String(localExpectCompletion).slice(0,10): localExpectCompletion}
                        onChange={(e) => setLocalExpectCompletion(e.target.value)}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="state"
                        name="state"
                        label="Etat"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={localState.labelFr}
                        disabled={true}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="type"
                        name="type"
                        label="Type"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={localType.labelFr}
                        disabled={true}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} color="secondary">
                    Annuler
                    </Button>
                    <Button type="submit" color="primary" variant="contained">
                    Enregistrer
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
      );
}
