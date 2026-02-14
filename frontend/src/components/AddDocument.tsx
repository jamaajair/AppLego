import React, { useState } from 'react';
import {
  Button, CircularProgress, Typography, Box,
  Paper, List, ListItem, ListItemText
} from '@mui/material';

import useDocument from '../hooks/useDocument';
import AddDocumentDialog from './AddDocumentDialog';

interface DossierDocumentsProps {
    dossierRef: string;
}

export default function AddDocument({ dossierRef }: DossierDocumentsProps) {
    const {
        loading,
        error,
        // dossierDocuments,
        documentTypes,
        documentStates,
        dialogOpen,
        openDialog,
        closeDialog,
        // loadDossierDocuments,
        addDocument
    } = useDocument(dossierRef);

    return (
        <Box sx={{
            p: 3,
            mx: 'auto',
            maxWidth: 800
        }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography variant="h5">Documents</Typography>
                <Button variant="contained" onClick={openDialog}>Ajouter un document</Button>
            </Box>

            {error && <Typography color="error">{error}</Typography>}

            {loading ? (
                <CircularProgress />
            ) : (
                <Paper elevation={2}>
                    {/* <List>
                        {dossierDocuments.length === 0 ? (
                            <ListItem>
                                <Typography>Aucun document pour ce dossier.</Typography>
                            </ListItem>
                        ) : (
                            dossierDocuments.map((doc) => (
                                <ListItem key={doc.ref} divider>
                                    <ListItemText primary={doc.label} />
                                    <ListItemText secondary={`Type: ${doc.type}`} />
                                    <ListItemText secondary={`État: ${doc.state}`} />
                                </ListItem>
                            ))
                        )}
                    </List> */}
                </Paper>
            )}

            <AddDocumentDialog
                open={dialogOpen}
                onClose={closeDialog}
                documentTypes={documentTypes}
                documentStates={documentStates}
                onSubmit={addDocument}
            />
        </Box>
    );
}