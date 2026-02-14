import React, { useState } from 'react';
import {
    Dialog, DialogActions, DialogContent, DialogTitle,
    Button, Select, MenuItem, FormControl, InputLabel, Typography, Box, TextField
} from '@mui/material';

interface AddDocumentDialogProps {
    open: boolean;
    onClose: () => void;
    documentTypes: {code: string, labelFr: string}[];
    documentStates: {code: string, labelFr: string}[];
    onSubmit: (type: string, state: string, CUL: string, CDIS?: string, URI?: string, file?: File, label?: string) => void;
}

export default function AddDocumentDialog({
    open,
    onClose,
    documentTypes,
    documentStates,
    onSubmit
}: AddDocumentDialogProps) {
    const CULTypes = ["CDIS", "URI", "LOCAL"];
    const [file, setFile] = useState<File | undefined>(undefined);
    const [selectedType, setSelectedType] = useState<string>('');
    const [selectedState, setSelectedState] = useState<string>('');
    const [CUL, setCUL] = useState<string>('');
    const [label, setLabel] = useState<string>('');
    const [CDIS, setCDIS] = useState<string | undefined>(undefined);
    const [URI, setURI] = useState<string | undefined>(undefined);
    const [error, setError] = useState<string>('');

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const selectedFile = event.target.files?.[0];
        if (selectedFile) {
            const maxSize = 10 * 1024 * 1024;
            const allowedTypes = ['application/pdf', 'image/jpeg', 'image/png'];

            if (selectedFile.size > maxSize) {
                setError('Fichier trop volumineux (max 10 Mo)');
                return;
            }

            if (!allowedTypes.includes(selectedFile.type)) {
                setError('Type de fichier non autorisé');
                return;
            }

            setFile(selectedFile);
            setError('');
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (!selectedType || !selectedState) {
            setError('Veuillez sélectionner un fichier, un type et un état');
            return;
        }

        onSubmit(selectedType, selectedState, CUL, CDIS, URI, file, label);
        setFile(undefined);
        setSelectedType('');
        setSelectedState('');
        setCUL('');
        setCDIS(undefined);
        setURI(undefined);
        setLabel('');
        setError('');
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>Ajouter un Document</DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        <FormControl fullWidth>
                            <InputLabel>CDIS / URI / LOCAL </InputLabel>
                            <Select
                                value={CUL}
                                onChange={(e) => setCUL(e.target.value)}
                            >
                                {CULTypes.map((type) => (
                                    <MenuItem key={type} value={type}>
                                        {type}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        {
                        (() => {
                            switch (CUL) {
                                case "LOCAL":
                                    return <input type="file" accept=".pdf,.jpg,.jpeg,.png" onChange={handleFileChange}/>;
                                case "CDIS":
                                    return <TextField fullWidth label="CDIS" value={CDIS} onChange={(e) => setCDIS(e.target.value)}/>;
                                case "URI":
                                    return <TextField fullWidth label="URI" value={URI} onChange={(e) => setURI(e.target.value)}/>;
                                default:
                                    return null;
                            }
                        }) ()
                        }

                        <TextField
                            fullWidth
                            label="Libellé du document"
                            value={label}
                            onChange={(e) => setLabel(e.target.value)}
                        />

                        <FormControl fullWidth>
                            <InputLabel>Type de document</InputLabel>
                            <Select
                                value={selectedType}
                                label="Type de document"
                                onChange={(e) => setSelectedType(e.target.value)}
                            >
                                {documentTypes.map((type) => (
                                    <MenuItem key={type.code} value={type.code}>
                                        {type.labelFr}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        <FormControl fullWidth>
                            <InputLabel>État du document</InputLabel>
                            <Select
                                value={selectedState}
                                label="État du document"
                                onChange={(e) => setSelectedState(e.target.value)}
                            >
                                {documentStates.map((state) => (
                                    <MenuItem key={state.code} value={state.code}>
                                        {state.labelFr}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        {error && (
                            <Typography color="error" variant="body2">
                                {error}
                            </Typography>
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