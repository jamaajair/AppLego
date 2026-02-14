import { useState, useEffect } from 'react';
import { api } from '../services/api';

export interface DocumentEntry {
    ref: number;
    label: string;
    type: string;
    state: string;
}

export default function useDocument(dossierRef: string) {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // const [dossierDocuments, setDossierDocuments] = useState<DocumentEntry[]>([]);
    const [documentTypes, setDocumentTypes] = useState<{code: string, labelFr: string}[]>([]);
    const [documentStates, setDocumentStates] = useState<{code: string, labelFr: string}[]>([]);

    const [dialogOpen, setDialogOpen] = useState(false);

    // const loadDossierDocuments = async () => {
    //     try {
    //         setLoading(true);
    //         const res = await api.get(`user/dossier/${dossierRef}/documents`);
            

    //         const formatted = res.data.map((doc: any) => ({
    //             ref: doc.ref,
    //             label: doc.label,
    //             type: doc.typeCode,
    //             state: doc.stateCode
    //         }));
            
    //         setDossierDocuments(formatted);
    //         setError(null);
    //     } catch (err) {
    //         console.error(err);
    //         setError(" -- Impossible de charger les documents du dossier -- ");
    //     } finally {
    //         setLoading(false);
    //     }
    // };

    const loadDialogData = async () => {
        try {
            setLoading(true);
            const resTypes = await api.get('/document/types');
            const resStates = await api.get('/document/states');
            const types = resTypes.data;
            const states = resStates.data;
            
            setDocumentTypes(types);
            setDocumentStates(states);
        } catch (err) {
            console.error(err);
            setError('Impossible de charger les métadonnées');
        } finally {
            setLoading(false);
        }
    };

    const addDocument = async (type: string, state: string, CUL: string, CDIS?: string, URI?: string, file?: File, label?: string) => {
        try {
            setLoading(true);
            
            console.log("CHECK URI" + URI);
            const formData = new FormData();
            formData.append("dossierRef", dossierRef);
            formData.append("documentTypeCode", type);
            formData.append("documentStateCode", state);
            if(label) formData.append("localDocumentLabel", label);
            formData.append("CDISorURIorLOCAL", CUL);
            if(CUL === "CDIS" && CDIS) formData.append("CDIS", CDIS);
            if(CUL === "URI" && URI) formData.append("URI", URI);
            if(CUL === "LOCAL" && file) formData.append("file", file);
            
            console.log("CUL=", CUL, "file=", file, "fileName=", file?.name, "size=", file?.size);
            await api.post('user/dossier/' + dossierRef + '/documents', formData);
        } catch (err) {
            console.error(err);
            setError("Erreur lors de l'ajout du document");
        } finally {
            setLoading(false);
        }
    };

    // const fileToBase64 = (file: File): Promise<string> => {
    //     return new Promise((resolve, reject) => {
    //         const reader = new FileReader();
    //         reader.readAsDataURL(file);
    //         reader.onload = () => resolve(reader.result as string);
    //         reader.onerror = (error) => reject(error);
    //     });
    // };

    // useEffect(() => {
    //     loadDossierDocuments();
    // }, []);
    
    return {
        loading,
        error,
        // dossierDocuments,
        documentTypes,
        documentStates,
        dialogOpen,
        openDialog: () => {
            setDialogOpen(true);
            loadDialogData();
        },
        closeDialog: () => setDialogOpen(false),
        // loadDossierDocuments,
        addDocument
    };
}