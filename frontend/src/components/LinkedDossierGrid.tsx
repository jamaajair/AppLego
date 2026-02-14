import { useState, useEffect, type FC, useCallback } from 'react';
import type { Dossier } from '../types/dossier.ts';
import { api } from '../services/api.ts';
import GenericDataGrid from '../components/ui/GenericDataGrid.tsx';
import type { GridColDef } from '@mui/x-data-grid';
import Typography from '@mui/material/Typography';
import {getLabelByLanguage} from "../services/utils.ts";
import { useNavigate } from 'react-router-dom';
import type {GridRowParams} from "@mui/x-data-grid";
import UnlinkDossierButton from './UnlinkDossierButton.tsx';
import { Box, IconButton } from '@mui/material';
import type { LinkedDossier } from '../types/linkedDossier.ts';
import CancelIcon from '@mui/icons-material/Cancel';

interface LinkedDossierGridProps {
    dossier: Dossier;
    linkedDossiersProp?: LinkedDossier[];
    onClickExtra?: (unlinkedDossierRef : String) => void;
}

const LinkedDossierGrid: React.FC<LinkedDossierGridProps> = ({dossier, linkedDossiersProp, onClickExtra}) => {
    const [loading, setLoading] = useState<boolean>(true);
    const [linkedDossiers, setLinkedDossiers] = useState<LinkedDossier[]>(linkedDossiersProp ?? []);
    const [columns, setColumns] = useState<GridColDef[]>([]);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const BASE_COLUMNS: GridColDef<Dossier>[] = [
        { field: 'ref', headerName: 'Référence', flex: 0.5 },
        { field: 'label', headerName: 'Label', flex: 1.2 },
        { field: 'comments', headerName: 'Commentaires', flex: 2 },
        {
            field: 'state',
            headerName: 'Etat',
            flex: 0.3,
            valueGetter: (value: unknown, row) =>{
                value = getLabelByLanguage(row?.state);
                return value;
            }
        },
        {
            field: 'type',
            headerName: 'Type',
            flex: 0.7,
            valueGetter: (value: unknown, row) => {
                value = getLabelByLanguage(row?.type);
                return value;
            }
        }
    ];

    const postUnlinkRequest = useCallback( async (childDossier: Dossier) => {
        try{
            const response = await api.delete("user/dossier/unlink", {
                "parentDossierRef": dossier.ref, 
                "childDossierRef": childDossier.ref 
            });

            if(!onClickExtra)
                setLinkedDossiers(prev => prev.filter(linkedDossier => linkedDossier.childDossier.ref !== childDossier.ref));
            else
                onClickExtra!(childDossier.ref); //only called if delete was successful
        } catch (err) {
            console.log(err);
        }
    }, [dossier.ref]);

    useEffect(() => {
        const fetchDossiers = async () => {
            try {
                if(!linkedDossiers || linkedDossiers.length == 0){
                    const dossier_id = dossier.ref;
                    const response = await api.get(`/user/linked_dossier/${dossier_id}`);
                    setLinkedDossiers(response.data);
                }

                setColumns([
                    ...BASE_COLUMNS,
                    {
                        field: 'unlink',
                        headerName: 'Unlink',
                        flex: 0.3,
                        sortable: false,
                        filterable: false,
                        disableColumnMenu: true,
                        renderCell: (params) => (
                            <Box sx={{ display: 'flex', justifyContent: 'stretch', width: '100%', height: '100%', alignItems: 'stretch', overflow: 'visible'}} onMouseDown={(event) => event.stopPropagation()}>
                                <IconButton 
                                    onClick={(event) => {event.stopPropagation(); postUnlinkRequest(params.row);}} 
                                    sx={{width: '100%', height: '100%', borderRadius: 0, padding: 0}} 
                                    color="error"> 
                                        <CancelIcon />
                                </IconButton>
                            </Box>
                        )
                    }

                ]);
            } catch (err) {
                setError('Failed to fetch dossiers.' + err);
            } finally {
                setLoading(false);
            }
        };

        void fetchDossiers();
    }, []);

    const handleRowClick = (params: GridRowParams<Dossier>) => {
        const id = params?.id;
        if (id) {
            navigate(`/user/dossiers/${id}`);
        }
    };

    useEffect(() => {
        if(linkedDossiersProp)
            setLinkedDossiers(linkedDossiersProp);
    }, [linkedDossiersProp]);

    useEffect(() => {
        setColumns([
            ...BASE_COLUMNS,
            {
                field: 'unlink',
                headerName: 'Unlink',
                flex: 0.3,
                sortable: false,
                filterable: false,
                disableColumnMenu: true,
                renderCell: (params) => (
                    <Box sx={{ display: 'flex', justifyContent: 'stretch', width: '100%', height: '100%', alignItems: 'stretch', overflow: 'visible'}} onMouseDown={(event) => event.stopPropagation()}>
                        <IconButton 
                            onClick={(event) => {event.stopPropagation(); postUnlinkRequest(params.row);}} 
                            sx={{width: '100%', height: '100%', borderRadius: 0, padding: 0}} 
                            color="error"> 
                                <CancelIcon />
                        </IconButton>
                    </Box>
                )
            }
        ])
    }, [dossier.ref]);

    return (
        <Box sx={{width: "50%", mx: 'auto', overflow: 'hidden'}}>
            <Typography variant="h4" gutterBottom>
                Linked Children Dossiers
            </Typography>

            <Typography variant="body2" gutterBottom>
                Cliquez sur une ligne pour voir le détail du dossier.
            </Typography>

            {loading ? (
                <p>Chargement...</p>
            ) : (
                <Box>
                    <GenericDataGrid<Dossier> rows={linkedDossiers.map(link => link.childDossier)} columns={columns} error={error} getRowId={(row) => row.ref}  onRowClick={handleRowClick} />
                </Box>
            )}        
        </Box>
    );
};

export default LinkedDossierGrid;