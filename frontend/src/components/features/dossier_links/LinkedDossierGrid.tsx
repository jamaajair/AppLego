import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Box, Typography, Alert, IconButton, Tooltip, Stack } from "@mui/material";
import CancelRoundedIcon from "@mui/icons-material/CancelRounded";
import type { GridColDef, GridRowParams } from "@mui/x-data-grid";
import { useNavigate } from "react-router-dom";

import type { Dossier } from "../../../types/dossier.ts";
import type { LinkedDossier } from "../../../types/linkedDossier.ts";
import { api } from "../../../services/api.ts";
import GenericDataGrid from "../../ui/GenericDataGrid.tsx";
import { getLabelByLanguage } from "../../../services/utils.ts";

interface LinkedDossierGridProps {
    dossier: Dossier;
    linkedDossiersProp?: LinkedDossier[];
    onClickExtra?: (unlinkedDossierRef: string) => void;
}

const LinkedDossierGrid: React.FC<LinkedDossierGridProps> = ({ dossier, linkedDossiersProp, onClickExtra }) => {
    const [loading, setLoading] = useState(true);
    const [linkedDossiers, setLinkedDossiers] = useState<LinkedDossier[]>(linkedDossiersProp ?? []);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const postUnlinkRequest = useCallback(
        async (childDossier: Dossier) => {
            try {
                await api.delete("user/dossier/unlink", {
                    data: {
                        parentDossierRef: dossier.ref,
                        childDossierRef: childDossier.ref,
                    },
                });

                if (onClickExtra) onClickExtra(childDossier.ref);
                else setLinkedDossiers((prev) => prev.filter((ld) => ld.childDossier.ref !== childDossier.ref));
            } catch (err) {
                console.log(err);
                setError("Impossible de délier le dossier.");
            }
        },
        [dossier.ref, onClickExtra]
    );

    const columns: GridColDef<Dossier>[] = useMemo(
        () => [
            { field: "ref", headerName: "Référence", flex: 0.6, minWidth: 120 },
            { field: "label", headerName: "Label", flex: 1.4, minWidth: 220 },
            { field: "comments", headerName: "Commentaires", flex: 2.2, minWidth: 260 },
            {
                field: "state",
                headerName: "État",
                flex: 0.6,
                minWidth: 140,
                valueGetter: (_v, row) => getLabelByLanguage(row?.state),
            },
            {
                field: "type",
                headerName: "Type",
                flex: 0.9,
                minWidth: 160,
                valueGetter: (_v, row) => getLabelByLanguage(row?.type),
            },
            {
                field: "unlink",
                headerName: "",
                width: 64,
                sortable: false,
                filterable: false,
                disableColumnMenu: true,
                align: "center",
                headerAlign: "center",
                renderCell: (params) => (
                    <Box onMouseDown={(e) => e.stopPropagation()}>
                        <Tooltip title="Délier">
                            <IconButton
                                size="small"
                                color="error"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    void postUnlinkRequest(params.row);
                                }}
                            >
                                <CancelRoundedIcon fontSize="small" />
                            </IconButton>
                        </Tooltip>
                    </Box>
                ),
            },
        ],
        [postUnlinkRequest]
    );

    useEffect(() => {
        const fetchLinked = async () => {
            try {
                if (!linkedDossiersProp) {
                    const response = await api.get(`/user/linked_dossier/${dossier.ref}`);
                    setLinkedDossiers(response.data ?? []);
                }
            } catch (err) {
                setError("Failed to fetch linked dossiers.");
            } finally {
                setLoading(false);
            }
        };

        void fetchLinked();
    }, [dossier.ref, linkedDossiersProp]);

    useEffect(() => {
        if (linkedDossiersProp) setLinkedDossiers(linkedDossiersProp);
    }, [linkedDossiersProp]);

    const rows = useMemo(() => linkedDossiers.map((l) => l.childDossier), [linkedDossiers]);

    const handleRowClick = (params: GridRowParams<Dossier>) => {
        const id = params?.id;
        if (id) navigate(`/user/dossiers/${id}`);
    };

    if (error) return <Alert severity="error">{error}</Alert>;

    return (
        <Stack spacing={1.5} sx={{ width: "100%" }}>
            <Box>
                <Typography variant="h6">Dossiers liés</Typography>
                <Typography variant="body2" color="text.secondary">
                    Cliquez une ligne pour ouvrir le dossier. Utilisez ✕ pour délier.
                </Typography>
            </Box>

            {loading ? (
                <Typography variant="body2">Chargement…</Typography>
            ) : rows.length === 0 ? (
                <Alert severity="info">Aucun dossier lié pour le moment.</Alert>
            ) : (
                <GenericDataGrid<Dossier>
                    rows={rows}
                    columns={columns}
                    error={null}
                    getRowId={(row) => row.ref}
                    onRowClick={handleRowClick}
                    rowHeight={80}
                />
            )}
        </Stack>
    );
};

export default LinkedDossierGrid;
