import { useState } from 'react';
import { Typography, Box, Alert, Button, TextField } from '@mui/material';
import UploadFileIcon from '@mui/icons-material/UploadFile';

interface FileUploadFormProps {
    title: string;
    description: string;
    onUpload: (file: File) => Promise<void>;
    fileValidator: (file: File) => boolean;
    errorMessage: string;
    uploading: boolean;
}

export default function FileUploadForm({
    title,
    description,
    onUpload,
    fileValidator,
    errorMessage,
    uploading,
}: FileUploadFormProps) {
    const [file, setFile] = useState<File | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const selectedFile = e.target.files[0];
            if (fileValidator(selectedFile)) {
                setFile(selectedFile);
                setError(null);
            } else {
                setError(errorMessage);
                setFile(null);
            }
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (file) {
            try {
                await onUpload(file);
            } catch (err) {
                // Errors are handled by the parent component
                console.error('Upload failed:', err);
            }
        } else {
            setError('Veuillez sélectionner un fichier d\'abord');
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ maxWidth: 800, mx: 'auto', mt: 3, p: 3 }}>
            <Typography variant="h5" gutterBottom sx={{ textAlign: 'center', mb: 3 }}>
                {title}
            </Typography>

            <Typography variant="body1" paragraph sx={{ mb: 3 }}>
                {description}
            </Typography>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>
            )}

            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                    type="file"
                    onChange={handleFileChange}
                    variant="outlined"
                    fullWidth
                    sx={{ mb: 2 }}
                />

                {file && (
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                        Fichier sélectionné: {file.name}
                    </Typography>
                )}

                <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                        disabled={!file || uploading}
                        startIcon={<UploadFileIcon />}
                    >
                        {uploading ? 'Déploiement en cours...' : 'Déployer'}
                    </Button>
                </Box>
            </Box>
        </Box>
    );
}
