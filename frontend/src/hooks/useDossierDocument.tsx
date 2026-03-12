import { useCallback, useEffect, useState } from "react";
import type { CodeDto } from "../types/config";
import { api } from "../services/api";
import type { DocumentAddFormValues } from "../types/documents/DocumentAddFormValues";
import type { DocumentListItemDto } from "../types/documents/DocumentListItemDto";
import type { GenericPageResponse } from "../types/genericPageResponse";

export default function useDossierDocument(dossierRef: string) {
    const [loading, setLoading] = useState(false);
    const [uploading, setUploading] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [documentTypes, setDocumentTypes] = useState<CodeDto[]>([]);
    const [documentRoles, setDocumentRoles] = useState<CodeDto[]>([]);
    const [documentStates, setDocumentStates] = useState<CodeDto[]>([]);
    const [pageData, setPageData] = useState<GenericPageResponse<DocumentListItemDto> | null>(null);

    
    const loadDocumentCodes = useCallback(async (): Promise<void> => {
        try{
            // setLoading(true);

            const [resTypes, resRoles, resStates] = await Promise.all([
                api.get<CodeDto[]>("/reference/document-types"),
                api.get<CodeDto[]>("/reference/document-roles"),
                api.get<CodeDto[]>("/reference/document-states")
            ]);

            setDocumentTypes(resTypes ?? []);
            setDocumentRoles(resRoles ?? []);
            setDocumentStates(resStates ?? []);

        } catch (err) {
            const msg = err instanceof Error ? err.message : "Failed to create document";
            throw new Error(msg);
        } finally {
            // setLoading(false);
        }
    }, []);

    //will have to redo load pages & generic data grid to better handle pages instead of all getting at once
    const loadDocuments = useCallback(async (page = 0, size = 200):Promise<void> =>{
        try {
            setLoading(true);
            const response = await api.get<GenericPageResponse<DocumentListItemDto>>("/user/dossier/" + dossierRef + "/documents", {params: {page, size}});
            setPageData(response);
        } finally {
            setLoading(false);
        }
    }, [dossierRef]);

    const createDocumentDossier = useCallback(async (values: DocumentAddFormValues, source: string): Promise<void> => {
        try{   
            setUploading(true);

            const formData = new FormData();
            formData.append("documentTypeCode", values.documentTypeCode);
            formData.append("documentStateCode", values.documentStateCode);
            formData.append("documentRoleCode", values.documentRoleCode);
            formData.append("localDocumentLabel", values.localDocumentLabel ?? "");
            formData.append("documentReferenceNotes", values.documentReferenceNotes ?? "");
            if(source === "URI") formData.append("URI", values.URI);
            if(source === "CDIS") formData.append("CDIS", values.CDIS);
            if(source === "LOCAL" && values.file) formData.append("file", values.file);

            const res = await api.post("user/dossier/" + dossierRef + "/documents", formData, {headers: {'Content-Type': 'multipart/form-data'}});

            await loadDocuments();
        } finally {
            setUploading(false);
        }
    }, [dossierRef]);

    const linkDocumentToOtherDossier = useCallback(async (documentRefId: number, otherDossierId: string, documentRoleCode: string): Promise<void> => {
        await api.post(`/user/document/${documentRefId}/dossier/${otherDossierId}/link`, {documentRoleCode});
        await loadDocuments();
    }, [dossierRef]);

    const unlinkDocumentFromDossier = useCallback(
        async (documentRefId: number): Promise<void> => {
        try {
            setDeleting(true);
            await api.delete(`/user/dossier/${dossierRef}/document/${documentRefId}`);

            await loadDocuments();
        } finally {
            setDeleting(false);
        }
    }, [dossierRef, loadDocuments]
    );

    useEffect(() => {
        loadDocuments();
    }, [loadDocuments]);

    useEffect(() => {
        loadDocumentCodes();
    }, [loadDocumentCodes]);

    return {
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
    };
} 