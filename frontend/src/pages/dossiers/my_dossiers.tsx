import { useEffect, useState, type FC } from "react";
import { useNavigate } from "react-router-dom";
import type { GridColDef, GridRowParams } from "@mui/x-data-grid";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Stack from "@mui/material/Stack";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import Breadcrumbs from "@mui/material/Breadcrumbs";
import Link from "@mui/material/Link";
import Chip from "@mui/material/Chip";
import Divider from "@mui/material/Divider";

import type { Dossier } from "../../types/dossier.ts";
import { api } from "../../services/api.ts";
import { loadColumnsFromJson } from "../../services/utils.ts";
import GenericDataGrid from "../../components/ui/GenericDataGrid.tsx";

const MyDossiers: FC = () => {
    const [dossiers, setDossiers] = useState<readonly Dossier[]>([]);
    const [columns, setColumns] = useState<GridColDef[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate();

    useEffect(() => {
        const fetchDossiers = async () => {
            try {
                setLoading(true);
                setError(null);

                const [response, cols] = await Promise.all([
                    api.get("/user/my_dossiers"),
                    loadColumnsFromJson("/config/my_dossiers_col.json"),
                ]);

                setDossiers(response.data || []);
                setColumns(cols);
            } catch (err) {
                setError(`Impossible de charger les dossiers. ${String(err)}`);
            } finally {
                setLoading(false);
            }
        };

        void fetchDossiers();
    }, []);

    const handleRowClick = (params: GridRowParams<Dossier>) => {
        const id = params?.id;
        if (id) navigate(`/user/dossiers/${id}`);
    };

    return (
        <Box sx={{ width: "100%", minWidth: 0, overflow: "hidden", flexDirection:"column", display:"flex" }}>
            <Stack spacing={1.5} sx={{ mb: 2 }}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link
                        underline="hover"
                        color="inherit"
                        onClick={() => navigate("/user/home")}
                        sx={{ cursor: "pointer" }}
                    >
                        Accueil
                    </Link>
                    <Typography color="text.primary">Mes dossiers</Typography>
                </Breadcrumbs>

                <Stack
                    direction={{ xs: "column", sm: "row" }}
                    alignItems={{ xs: "stretch", sm: "center" }}
                    justifyContent="space-between"
                    spacing={2}
                >
                    <Box sx={{ minWidth: 0 }}>
                        <Typography variant="h4" sx={{ fontWeight: 900, letterSpacing: -0.3 }}>
                            Mes dossiers
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Cliquez sur une ligne pour consulter le détail d’un dossier.
                        </Typography>
                    </Box>

                    <Stack direction="row" spacing={1} justifyContent="flex-end">
                        <Button
                            variant="contained"
                            startIcon={<AddIcon />}
                            onClick={() => navigate("/user/dossiers/new")}
                        >
                            Nouveau dossier
                        </Button>
                    </Stack>
                </Stack>

                <Divider />

                <Stack direction="row" spacing={1} alignItems="center" sx={{ flexWrap: "wrap", paddingLeft: "2px" }}>
                    <Chip size="small" label={`${dossiers.length} dossiers en cours`} variant="outlined" />
                </Stack>
            </Stack>

            <Box sx={{ flexGrow: 1, minHeight: 0 }}>
                <GenericDataGrid<Dossier>
                    rows={dossiers}
                    columns={columns}
                    loading={loading}
                    error={error}
                    getRowId={(row) => row.ref}
                    onRowClick={handleRowClick}
                    toolbar
                />
            </Box>
        </Box>
    );
};

export default MyDossiers;
