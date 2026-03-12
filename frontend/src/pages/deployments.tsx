import { useState, useEffect } from 'react';
import { Typography, Box, Alert, Snackbar, Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeploymentList from '../components/DeploymentList';
import useDeployment from '../hooks/useDeployment';
import type { Deployment } from '../models/deployment';
import { useNavigate } from 'react-router-dom';
import { loadColumnsFromJson } from '../services/utils';
import type { GridColDef } from '@mui/x-data-grid';

export default function DeploymentsPage() {
    const {
        deployments,
        loading,
        error,
        deployProcess,
        deleteDeployment,
        loadDeployments
    } = useDeployment();
    const navigate = useNavigate();

    const [columns, setColumns] = useState<GridColDef[]>([]);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState<'success' | 'error'>('success');

    useEffect(() => {
        loadDeployments();
        const fetchColumns = async () => {
            try {
                const cols = await loadColumnsFromJson('/config/deployments_col.json');
                setColumns(cols);
            } catch (err) {
                console.error('Failed to load columns:', err);
            }
        };
        void fetchColumns();
    }, []); // Empty dependency array - only run once on mount

    const handleDelete = async (deploymentId: string) => {
        try {
            await deleteDeployment(deploymentId);
            setSnackbarMessage('Processus supprimé avec succès !');
            setSnackbarSeverity('success');
            setSnackbarOpen(true);
            await loadDeployments(); // Refresh the list
        } catch (err) {
            console.error('Échec de la suppression:', err);
            setSnackbarMessage('Échec de la suppression du processus');
            setSnackbarSeverity('error');
            setSnackbarOpen(true);
        }
    };

    const handleViewDetails = (deploymentId: string) => {
        // TODO: Implement detail view or navigation
    };

    const handleAddDeployment = () => {
        navigate('/user/deployments/new');
    };

    const handleCloseSnackbar = () => {
        setSnackbarOpen(false);
    };

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" gutterBottom>
                Gestion des processus BPMN
            </Typography>

            <Typography variant="body1" paragraph>
                Déployez vos processus BPMN pour les utiliser dans le moteur Flowable.
            </Typography>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>
            )}

            <Button
                variant="contained"
                color="primary"
                onClick={handleAddDeployment}
                startIcon={<AddIcon />}
                sx={{ mb: 2 }}
            >
                Ajouter un processus
            </Button>

            <DeploymentList
                deployments={deployments}
                loading={loading}
                error={error}
                onDelete={handleDelete}
                onViewDetails={handleViewDetails}
            />

            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert
                    onClose={handleCloseSnackbar}
                    severity={snackbarSeverity}
                    sx={{ width: '100%' }}
                >
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </Box>
    );
}