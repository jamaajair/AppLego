import React, { useState } from 'react';
import {
Dialog, DialogActions, DialogContent, DialogTitle,
Button, Select, MenuItem, FormControl, InputLabel, Typography, Box
} from '@mui/material';
import type { StakeholderEntry } from '../hooks/useStakeholder';
import type {CodeDto} from "../types/config.ts";

interface AddStakeholderDialogProps {
    open: boolean;
    onClose: () => void;
    parties: StakeholderEntry[];
    roles: CodeDto[];
    onSubmit: (partyRef: number, role?: string) => void;
}

export default function AddStakeholderDialog({
    open,
    onClose,
    parties,
    roles,
    onSubmit
    }: AddStakeholderDialogProps) {

    const [selectedParty, setSelectedParty] = useState<number | "">("");
    const [selectedRole, setSelectedRole] = useState<string>("");
    const [error, setError] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (!selectedParty) {
            setError("Veuillez sélectionner une partie");
            return;
        }

        onSubmit(Number(selectedParty), selectedRole || undefined);
        setSelectedParty("");
        setSelectedRole("");
        setError("");
        };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
        <DialogTitle>Ajouter une Partie Prenante</DialogTitle>

        <form onSubmit={handleSubmit}>
            <DialogContent>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>

                <FormControl fullWidth>
                <InputLabel>Partie</InputLabel>
                <Select
                value={selectedParty}
                label="Partie"
                onChange={(e) => setSelectedParty(e.target.value as number)}
                >
                {parties.map((p) => (
                  <MenuItem key={p.ref} value={p.ref}>
                    {p.label} ({p.kind})
                  </MenuItem>
                ))}
                </Select>
                </FormControl>

                <FormControl fullWidth>
                    <InputLabel>Rôle</InputLabel>

                    <Select
                        value={selectedRole}
                        label="Rôle"
                        onChange={(e) => setSelectedRole(e.target.value)}
                        >
                        {roles.map((role) => (
                          <MenuItem key={role.code} value={role.code}>
                            {role.labelFr}
                          </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                {error && (
                <Typography color="error" variant="body2">{error}</Typography>
                )}

                </Box>
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose} variant="outlined">Annuler</Button>
                <Button type="submit" variant="contained">Ajouter</Button>
            </DialogActions>
        </form>
        </Dialog>
    );
}
