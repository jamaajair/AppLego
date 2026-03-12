import { useCallback, useEffect, useState } from "react";
import { api } from "../services/api";
import type { Dossier } from "../types/dossier";
import type { LinkedDossier } from "../types/linkedDossier";
import type { Code } from "../types/code";
import type { PageState } from "../types/dossier";

export function useLinkUnlinkForkPage(id: string) {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [state, setState] = useState<PageState>({
        dossier: null,
        linkedDossiers: [],
        dossiers: [],
        linkKinds: [],
    });

    const refresh = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            const [dossier, linkedDossiers, dossiers, linkKinds] = await Promise.all([
                api.get<Dossier>(`/user/dossier/${id}`),
                api.get<LinkedDossier[]>(`/user/linked_dossier/${id}`),
                api.get<Dossier[]>("user/my_dossiers"),
                api.get<Code[]>("user/code/LINK_KIND"),
            ]);

            setState({
                dossier: dossier ?? null,
                linkedDossiers: linkedDossiers ?? [],
                dossiers: dossiers ?? [],
                linkKinds: linkKinds ?? [],
            });
        } catch (e) {
            console.error(e);
            setError(`Unable to fetch dossier details`);
        } finally {
            setLoading(false);
        }
    }, [id]);

    useEffect(() => {
        void refresh();
    }, [refresh]);

    // helpers mutation locale
    const addLinkedDossierLocal = useCallback((newLink: LinkedDossier) => {
        setState((prev) => ({ ...prev, linkedDossiers: [...prev.linkedDossiers, newLink] }));
    }, []);

    const removeLinkedChildLocal = useCallback((childRef: string) => {
        setState((prev) => ({
            ...prev,
            linkedDossiers: prev.linkedDossiers.filter((l) => l.childDossier.ref !== childRef),
        }));
    }, []);

    return {
        loading,
        error,
        ...state,
        refresh,
        addLinkedDossierLocal,
        removeLinkedChildLocal,
    };
}
