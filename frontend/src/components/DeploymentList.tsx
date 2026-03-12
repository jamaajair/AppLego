import { useState, useEffect } from 'react';
import { Box, IconButton, Tooltip, Typography } from '@mui/material';
import type { GridColDef } from '@mui/x-data-grid';
import DeleteIcon from '@mui/icons-material/Delete';
import VisibilityIcon from '@mui/icons-material/Visibility';
import type { Deployment } from '../models/deployment';
import { formatEuropeanDate } from '../services/utils';
import GenericDataGrid from './ui/GenericDataGrid';

interface DeploymentListProps {
    deployments: Deployment[];
    loading: boolean;
    error: string | null;
    onDelete: (deploymentId: string) => Promise<void>;
    onViewDetails: (deploymentId: string) => void;
}

export default function DeploymentList({
    deployments,
    loading,
    error,
    onDelete,
    onViewDetails
}: DeploymentListProps) {
    const [deletingId, setDeletingId] = useState<string | null>(null);
    const [columns, setColumns] = useState<GridColDef[]>([]);

    useEffect(() => {
        // Define columns with actions
        setColumns([
            {
                field: 'deploymentId',
                headerName: 'ID',
                flex: 0.8
            },
            {
                field: 'name',
                headerName: 'Nom',
                flex: 1.5
            },
            {
                field: 'deploymentTime',
                headerName: 'Date de déploiement',
                flex: 0.8,
                valueFormatter: (params: string) => {
                    try {
                        if (!params) {
                            return 'N/A';
                        }
                        return formatEuropeanDate(params);
                    } catch (error) {
                        console.error('Error formatting date:', error, 'for value:', params);
                        return 'N/A';
                    }
                }
            },
            {
                field: 'actions',
                headerName: 'Actions',
                flex: 0.5,
                sortable: false,
                filterable: false,
                disableColumnMenu: true,
                renderCell: (params) => (
                    <Box display="flex" gap="8px">
                        <Tooltip title="Voir les détails">
                            <IconButton
                                onClick={() => onViewDetails(params.row.deploymentId)}
                                color="primary"
                                size="small"
                            >
                                <VisibilityIcon fontSize="small" />
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Supprimer">
                            <IconButton
                                onClick={() => handleDelete(params.row.deploymentId)}
                                color="error"
                                size="small"
                                disabled={deletingId === params.row.deploymentId}
                            >
                                <DeleteIcon fontSize="small" />
                            </IconButton>
                        </Tooltip>
                    </Box>
                )
            }
        ]);
    }, []);

    const handleDelete = async (deploymentId: string) => {
        try {
            setDeletingId(deploymentId);
            await onDelete(deploymentId);
        } catch (err) {
            console.error('Échec de la suppression:', err);
        } finally {
            setDeletingId(null);
        }
    };

    return (
        <Box display="flex" flexDirection="column" gap="20px" mt="20px">
            <Typography variant="h6" gutterBottom>
                Processus déployés
            </Typography>

            <GenericDataGrid<Deployment>
                rows={deployments}
                columns={columns}
                loading={loading}
                error={error}
                getRowId={(row) => row.deploymentId}
                height={400}  // Custom height for deployments page
            />
        </Box>
    );
}