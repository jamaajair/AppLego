import { useCallback, useState } from "react";
import { api } from "../services/api";
import type { LinkedDossier, LinkPayload } from "../types/linkedDossier";

export function useDossierLinksActions() {
    const [busy, setBusy] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const forkDossier = useCallback(async (dossierRef: string) => {
        setBusy(true);
        setError(null);
        try {
            return await api.post<LinkedDossier, Record<string, never>>(
                `user/dossier/${dossierRef}/fork`,
                {}
            );
        } catch (e) {
            console.error(e);
            setError("Unable to fork dossier");
            throw e;
        } finally {
            setBusy(false);
        }
    }, []);

    const linkDossiers = useCallback(async (payload: LinkPayload) => {
        setBusy(true);
        setError(null);
        try {
            return await api.post<LinkedDossier, LinkPayload>("user/dossier/link", payload);
        } catch (e) {
            console.error(e);
            setError("Unable to create dossier link");
            throw e;
        } finally {
            setBusy(false);
        }
    }, []);

    const unlinkDossiers = useCallback(async (parentRef: string, childRef: string) => {
        setBusy(true);
        setError(null);
        try {
            await api.delete<void, { parentDossierRef: string; childDossierRef: string }>(
                "user/dossier/unlink",
                { parentDossierRef: parentRef, childDossierRef: childRef }
            );
        } catch (e) {
            console.error(e);
            setError("Unable to unlink dossier");
            throw e;
        } finally {
            setBusy(false);
        }
    }, []);

    return { busy, error, forkDossier, linkDossiers, unlinkDossiers };
}
