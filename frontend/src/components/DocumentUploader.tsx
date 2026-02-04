import React, { useState } from 'react';
import { 
  Button, 
  Typography, 
  Box, 
  TextField, 
  CircularProgress 
} from '@mui/material';
import { api } from '../services/api';

interface DocumentUploaderProps {
  dossierRef: string;
  onUploadSuccess?: () => void;
}

const DocumentUploader: React.FC<DocumentUploaderProps> = ({ 
  dossierRef, 
  onUploadSuccess 
}) => {
  const [file, setFile] = useState<File | null>(null);
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = event.target.files?.[0];
    if (selectedFile) {
      // Validation de base
      const maxSize = 10 * 1024 * 1024; // 10 Mo
      const allowedTypes = ['application/pdf', 'image/jpeg', 'image/png'];

      if (selectedFile.size > maxSize) {
        setError('Fichier trop volumineux. Max 10 Mo.');
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

  const handleUpload = async () => {
    if (!file) {
      setError('Aucun fichier sélectionné');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('description', description);

    setLoading(true);
    setError('');

    try {
      const response = await api.post(`/dossiers/${dossierRef}/documents`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      // Réinitialiser le formulaire
      setFile(null);
      setDescription('');
      
      // Callback optionnel pour rafraîchir la liste des documents
      onUploadSuccess?.();
    } catch (err) {
      setError('Erreur lors du téléchargement');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
      <Typography variant="h6">Téléverser un document</Typography>
      
      <input
        accept=".pdf,.jpg,.jpeg,.png"
        type="file"
        onChange={handleFileChange}
      />

      <TextField
        label="Description du document"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        fullWidth
        variant="outlined"
      />

      {error && (
        <Typography color="error" variant="body2">
          {error}
        </Typography>
      )}

      <Button
        variant="contained"
        onClick={handleUpload}
        disabled={!file || loading}
        startIcon={loading ? <CircularProgress size={20} /> : null}
      >
        {loading ? 'Téléversement en cours...' : 'Téléverser'}
      </Button>
    </Box>
  );
};

export default DocumentUploader;