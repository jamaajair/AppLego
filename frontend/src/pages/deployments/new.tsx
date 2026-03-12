import { useState } from 'react';
import { Snackbar, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { isValidBpmnFile } from '../../services/utils';
import { api } from '../../services/api';
import FileUploadForm from '../../components/FileUploadForm';

export default function NewDeploymentPage() {
    const navigate = useNavigate();
    const [uploading, setUploading] = useState(false);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState<'success' | 'error'>('success');

    const handleUpload = async (file: File) => {
        try {
            setUploading(true);
            
            await api.upload('/deployments', file);
            
            setSnackbarMessage('Processus déployé avec succès !');
            setSnackbarSeverity('success');
            setSnackbarOpen(true);
            setTimeout(() => {
                navigate('/user/deployments');
            }, 3000);
        } catch (err) {
            console.error('Erreur lors du déploiement:', err);
            setSnackbarMessage('Échec du déploiement du processus. Veuillez réessayer.');
            setSnackbarSeverity('error');
            setSnackbarOpen(true);
        } finally {
            setUploading(false);
        }
    };

    return (
        <>
            <FileUploadForm
                title="Nouveau déploiement BPMN"
                description="Sélectionnez un fichier BPMN (.bpmn20.xml) pour le déployer dans le moteur Flowable."
                onUpload={handleUpload}
                fileValidator={isValidBpmnFile}
                errorMessage="Seuls les fichiers .bpmn20.xml sont acceptés"
                uploading={uploading}
            />
            
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={() => setSnackbarOpen(false)}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert severity={snackbarSeverity} sx={{ width: '100%' }} onClose={() => setSnackbarOpen(false)}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </>
    );
}
