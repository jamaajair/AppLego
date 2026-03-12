import React from "react";
import { useParams } from "react-router-dom";
import { Stack } from "@mui/material";
import PageShell from "../../components/layout/PageShell.tsx";
import AppError from "../../components/ui/AppError.tsx";

import PageCard from "../../components/ui/PageCard.tsx";
import GenericEdit from "../../components/ui/GenericEdit";
import DossierStakeholdersPanel from "../../components/features/stakeholders/DossierStakeholdersPanel.tsx";
import LinkUnlinkForkComp from "../../components/features/dossier_links/LinkUnlinkForkComp.tsx";
import DocumentPanel from "../../components/features/document/DocumentPanel.tsx";
import useDossierDocument from "../../hooks/useDossierDocument.tsx";

const Dossier: React.FC = () => {
    const { id } = useParams();
    const {
        loading,
        uploading,
        deleting,
        pageData,
        documentTypes,
        documentRoles,
        documentStates,
        createDocumentDossier,
        loadDocuments,
        unlinkDocumentFromDossier,
        linkDocumentToOtherDossier
    } = useDossierDocument(id ?? "");

    if (!id) {
        return (
            <PageShell title={"Dossier"}>
                <AppError message={"Dossier not found."} centered />
            </PageShell>
        );
    }

    return (
        <PageShell title={"Dossier " + id}>
            <Stack spacing={2}>
                <PageCard title="Informations">
                    <GenericEdit
                        configPath="/config/dossier.json"
                        dataUrl={`/user/dossier/${id}`}
                        submitUrl={`/user/dossier/${id}`}
                    />
                </PageCard>

                <PageCard title="Parties prenantes">
                    <DossierStakeholdersPanel dossierRef={id} />
                </PageCard>

                <PageCard title="Documents associés">
                    <DocumentPanel
                    dossierRef={id}
                    loading={loading}
                    uploading={uploading}
                    deleting={deleting}
                    pageData={pageData}
                    documentTypes={documentTypes}
                    documentStates={documentStates}
                    documentRoles={documentRoles}
                    createDocumentDossier={createDocumentDossier}
                    unlinkDocumentFromDossier={unlinkDocumentFromDossier}
                    linkDocumentToOtherDossier={linkDocumentToOtherDossier}
                    />
                </PageCard>

                <PageCard title="Liens / Fork">
                    <LinkUnlinkForkComp id={id} />
                </PageCard>
            </Stack>
        </PageShell>
    );
};

export default Dossier;
