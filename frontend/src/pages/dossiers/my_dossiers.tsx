import { useState, useEffect, type FC } from 'react';
import type { Dossier } from '../../types/dossier.ts';
import { api } from '../../services/api.ts';
import GenericDataGrid from '../../components/ui/GenericDataGrid.tsx';
import type { GridColDef } from '@mui/x-data-grid';
import Typography from '@mui/material/Typography';
import {loadColumnsFromJson} from "../../services/utils.ts";
import { useNavigate } from 'react-router-dom';
import type {GridRowParams} from "@mui/x-data-grid";
import Button from '@mui/material/Button';
import AddIcon from '@mui/icons-material/Add';
import type { LinkKind } from '../../types/linkKind.ts';



const MyDossiers: FC = () => {
    const [dossiers, setDossiers] = useState<readonly Dossier[]>([]);
    const [columns, setColumns] = useState<GridColDef[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchDossiers = async () => {
            try {
                const response = await api.get('/user/my_dossiers');
                const dossiers = response.data || [];
                console.log("Response: ", response.data)
                setDossiers(dossiers);

                const cols = await loadColumnsFromJson("/config/my_dossiers_col.json");
                setColumns(cols);
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

    return (
        <div>
            <Typography variant="h4" gutterBottom>
                Mes Dossiers
            </Typography>

            <Typography variant="body2" gutterBottom>
                Cliquez sur une ligne pour voir le détail du dossier.
            </Typography>

            <Button
                variant={"contained"}
                color={"primary"}
                onClick={() => navigate('/user/dossiers/new')}
                sx={{mb:2}}
            >
                <AddIcon />
            </Button>

            {loading ? (
                <p>Chargement...</p>
            ) : (
                <GenericDataGrid<Dossier> rows={dossiers} columns={columns} error={error} getRowId={(row) => row.ref}  onRowClick={handleRowClick} />
            )}
        </div>
    );
};

export default MyDossiers;