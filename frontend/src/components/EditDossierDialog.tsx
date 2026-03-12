import React, { useEffect, useMemo, useState } from "react";
import {
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Typography,
    Box,
    Stack,
    Divider,
    Autocomplete,
    TextField,
    Chip
} from "@mui/material";
import type { StakeholderEntry } from "../hooks/useStakeholder";
import type { CodeDto } from "../types/config.ts";

interface AddStakeholderDialogProps {
    open: boolean;
    onClose: () => void;
    parties: StakeholderEntry[];
    existingParties?: StakeholderEntry[] | number[];
    roles: CodeDto[];
    onSubmit: (partyRef: number, role?: string) => void;
}

export default function AddStakeholderDialog({
                                                 open,
                                                 onClose,
                                                 parties,
                                                 existingParties,
                                                 roles,
                                                 onSubmit
                                             }: AddStakeholderDialogProps) {
    const [selectedParty, setSelectedParty] = useState<StakeholderEntry | null>(null);
    const [selectedRole, setSelectedRole] = useState<string>("");
    const [error, setError] = useState("");

    useEffect(() => {
        if (!open) {
            setSelectedParty(null);
            setSelectedRole("");
            setError("");
        }
    }, [open]);

    const excludedRefs = useMemo(() => {
        if (!existingParties || existingParties.length === 0) return new Set<number>();
        const refs = new Set<number>();
        if (typeof existingParties[0] === "number") {
            (existingParties as number[]).forEach((r) => refs.add(Number(r)));
        } else {
            (existingParties as StakeholderEntry[]).forEach((p) => refs.add(Number(p.ref)));
        }
        return refs;
    }, [existingParties]);

    const filteredParties = useMemo(
        () => parties.filter((p) => !excludedRefs.has(Number(p.ref))),
        [parties, excludedRefs]
    );

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (!selectedParty) {
            setError("Veuillez sélectionner une partie.");
            return;
        }

        onSubmit(Number(selectedParty.ref), selectedRole || undefined);
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>Ajouter une partie prenante</DialogTitle>
            <Divider />

            <form onSubmit={handleSubmit}>
                <DialogContent sx={{ pt: 2.5 }}>
                    <Stack spacing={2}>
                        <Box>
                            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                                Choisissez une partie, puis éventuellement un rôle.
                            </Typography>

                            <Autocomplete
                                options={filteredParties}
                                value={selectedParty}
                                onChange={(_, v) => {
                                    setSelectedParty(v);
                                    setError("");
                                }}
                                getOptionLabel={(o) => o.label ?? String(o.ref)}
                                isOptionEqualToValue={(o, v) => Number(o.ref) === Number(v.ref)}
                                noOptionsText={
                                    parties.length === 0
                                        ? "Aucune partie disponible."
                                        : "Toutes les parties disponibles ont déjà été ajoutées."
                                }
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        label="Partie"
                                        placeholder="Rechercher…"
                                        error={Boolean(error)}
                                    />
                                )}
                            />
                        </Box>

                        <FormControl fullWidth>
                            <InputLabel>Rôle</InputLabel>
                            <Select
                                value={selectedRole}
                                label="Rôle"
                                onChange={(e) => setSelectedRole(String(e.target.value))}
                            >
                                <MenuItem value="">
                                    <em>Aucun</em>
                                </MenuItem>
                                {roles.map((r) => (
                                    <MenuItem key={r.code} value={r.code}>
                                        {r.labelFr}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        {selectedParty && (
                            <Box>
                                <Typography variant="caption" color="text.secondary">
                                    Sélection :
                                </Typography>
                                <Stack direction="row" spacing={1} sx={{ mt: 0.5, flexWrap: "wrap" }}>
                                    <Chip label={selectedParty.label} />
                                    {selectedRole && (
                                        <Chip
                                            label={`Rôle : ${roles.find((r) => r.code === selectedRole)?.labelFr ?? selectedRole}`}
                                            variant="outlined"
                                        />
                                    )}
                                </Stack>
                            </Box>
                        )}

                        {error && <Typography color="error" variant="body2">{error}</Typography>}
                    </Stack>
                </DialogContent>

                <DialogActions sx={{ px: 3, pb: 2.5 }}>
                    <Button onClick={onClose} variant="outlined">
                        Annuler
                    </Button>
                    <Button type="submit" variant="contained" disabled={!selectedParty}>
                        Ajouter
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
}
