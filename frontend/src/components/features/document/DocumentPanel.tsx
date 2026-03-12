import React, { useEffect, useMemo, useState } from 'react';
import { Box, Button, Card, CardHeader, Divider, IconButton, Typography } from '@mui/material';
import AddIcon from "@mui/icons-material/Add";
import { AppButton } from '../../ui/AppButton';
import { DocumentAddDialog } from './DocumentAddDialog';
import type { CodeDto } from '../../../types/config';
import type { DocumentAddFormValues } from '../../../types/documents/DocumentAddFormValues';
import GenericDataGrid from '../../ui/GenericDataGrid';
import type { DocumentListItemDto } from '../../../types/documents/DocumentListItemDto';
import type { GenericPageResponse } from '../../../types/genericPageResponse';
import type { GridColDef } from '@mui/x-data-grid';
import ConfirmDeleteDialog from '../../ui/ConfirmDeleteDialog';
import LinkOffIcon from '@mui/icons-material/LinkOff';
import AddLinkIcon from '@mui/icons-material/AddLink';
import DocumentLinkDialog from './DocumentLinkDialog';
import type { Dossier } from '../../../types/dossier';
import { api } from '../../../services/api';

interface DocumentProps {
    dossierRef: string;
    loading: boolean;
    uploading: boolean;
    deleting: boolean;
    pageData: GenericPageResponse<DocumentListItemDto> | null;
    documentTypes: CodeDto[];
    documentStates: CodeDto[];
    documentRoles: CodeDto[];
    createDocumentDossier: (values: DocumentAddFormValues, source: string) => Promise<void>;
    unlinkDocumentFromDossier: (documentRefId: number) => Promise<void>;
    linkDocumentToOtherDossier: (documentRefId: number, otherDossierId: string, documentRoleCode: string) => Promise<void>;
}

const DocumentPanel : React.FC<DocumentProps> = ({dossierRef, loading, uploading, deleting, pageData, documentTypes, documentStates, documentRoles, createDocumentDossier, unlinkDocumentFromDossier, linkDocumentToOtherDossier}) => {
    const [openAddDialog, setOpentAddDialog] = useState<boolean>(false);
    const [openLinkDialog, setOpenLinkDialog] = useState<boolean>(false);
    const [deleteOpen, setDeleteOpen] = useState<boolean>(false);
    const [selectedRow, setSelectedRow] = useState<DocumentListItemDto | null>(null);
    const [dossiers, setDossiers] = useState<Dossier[]>([]);
    const [dossiersLoading, setDossiersLoading] = useState<boolean>(false);

    //might change later, since other panels would need a list of dossiers owned by user => have to restructure later (maybe not a big deal if we get dossiers multiple times)
    useEffect(() => {
        const fetchDossiers = async () => {
            try {
            setDossiersLoading(true);
            const res = await api.get<{ data: Dossier[] }>("/user/my_dossiers");
            setDossiers(res?.data ?? []);
            } finally {
            setDossiersLoading(false);
            }
        };
        fetchDossiers();
    }, []);

    
    const columns = useMemo<GridColDef<DocumentListItemDto>[]>(
        () => [
        { field: "ref", headerName: "Ref", flex: 1, minWidth: 90 },
        { field: "notes", headerName: "Notes", flex: 2, minWidth: 200 },
        {
            field: "isDocLocal",
            headerName: "Local",
            flex: 1,
            minWidth: 110,
            renderCell: (params) => (params.row.isDocLocal === 1 ? "Yes" : "No"),
        },
        {
            field: "actions",
            headerName: "",
            sortable: false,
            filterable: false,
            width: 100,
            align: "center",
            headerAlign: "center",
            renderCell: (params) => (
            <>
                <IconButton
                size="small"
                aria-label="unlink document"
                disabled={uploading || deleting }
                onClick={(e) => {
                    e.stopPropagation();
                    setSelectedRow(params.row);
                    setDeleteOpen(true);
                }}
                >
                    <LinkOffIcon fontSize="small" />
                </IconButton>

                <IconButton
                size="small"
                aria-label="link document"
                disabled={uploading || deleting}
                onClick={(e) => {
                    e.stopPropagation();
                    setSelectedRow(params.row);
                    setOpenLinkDialog(true);
                }}
                >
                    <AddLinkIcon fontSize="small" />
                </IconButton>
            </>
            ),
        },
        ],
        [uploading, deleting]
    );

    return(
        <Box>
            <Box
            display="flex"
            justifyContent="space-between"
            alignItems="center"
            >
                <Typography variant="h6">
                Documents
                </Typography>

                <AppButton
                intent="primary"
                startIcon={<AddIcon />}
                size="small"
                onClick={() => setOpentAddDialog(true)}>
                Ajouter
                </AppButton>
            </Box>

            <Divider/>

            <DocumentAddDialog 
            open={openAddDialog}
            uploading={uploading}
            onClose={() => setOpentAddDialog(false)}
            onSubmit={createDocumentDossier}
            documentTypes={documentTypes}
            documentRoles={documentRoles}
            documentStates={documentStates}
            />

            <DocumentLinkDialog
            open={openLinkDialog}
            loading={dossiersLoading}
            onClose={() => {
                setOpenLinkDialog(false);
                setSelectedRow(null);
            }}
            onSubmit={async (otherDossierId, documentRoleCode) => {
                if (!selectedRow) return;

                await linkDocumentToOtherDossier(selectedRow.ref, otherDossierId, documentRoleCode);
                setOpenLinkDialog(false);
                setSelectedRow(null);
            }}
            dossiers={dossiers}
            excludeDossierId={dossierRef}
            documentRoles={documentRoles}
            />

            <ConfirmDeleteDialog
            open={deleteOpen}
            loading={deleting}
            title="Délier le document"
            message={
            selectedRow
                ? `Voulez-vous délier le document ${selectedRow.ref} de ce dossier ?`
                : "Voulez-vous délier ce document de ce dossier ?"
            }
            confirmLabel="Délier"
            cancelLabel="Annuler"
            onCancel={() => {
            if (deleting) return;
            setDeleteOpen(false);
            setSelectedRow(null);
            }}
            onConfirm={async () => {
            if (!selectedRow) return;
            await unlinkDocumentFromDossier(selectedRow.ref);
            setDeleteOpen(false);
            setSelectedRow(null);
            }}
            />

            <GenericDataGrid
            rows={pageData?.content || []}
            columns={columns}
            loading={loading}
            getRowId={(row) => row.ref}
            height={315}
            rowHeight={35}
            pageSizeOptions={[10]}
            />
        </Box>
    );
};

export default DocumentPanel;