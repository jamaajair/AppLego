import { useState, useEffect, useCallback } from 'react';
import { api } from '../services/api';
import { type CodeDto } from '../types/config';

export interface StakeholderEntry {
    ref: number;
    label: string;
    kind: string;
    role?: CodeDto | null;
}

export default function useStakeholder(dossierRef: string) {

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const [dossierParties, setDossierParties] = useState<StakeholderEntry[]>([]);
    const [availableParties, setAvailableParties] = useState<StakeholderEntry[]>([]);
    const [roles, setRoles] = useState<CodeDto[]>([]);

    const [dialogOpen, setDialogOpen] = useState(false);

    const loadDossierParties = useCallback(async () => {
        try {
            setLoading(true);

            const res = await api.get<StakeholderEntry[]>(`/user/dossiers/${dossierRef}/parties`);

            const formatted = res.map((p) => ({
                ref: p.partyRef,
                label: p.label,
                kind: p.kind ?? "Undefined",
                role: p.role ?? null
            }));

            setDossierParties(formatted);
            setError(null);

        } catch (err) {
            console.error(err);
            setError("Unable to load dossier stakeholders");
        } finally {
            setLoading(false);
        }
    }, [dossierRef]);

    const loadDialogData = async () => {
        try {
            setLoading(true);
            const partiesResponse = await api.get<StakeholderEntry[]>(`/user/dossiers/parties`);
            setAvailableParties(partiesResponse || []);
        } catch (err) {
            console.error(err);
            setError('Unable to load available parties');
        } finally {
            setLoading(false);
        }
    };

    const addStakeholder = async (partyRef: number, role?: string) => {
        try {
            setLoading(true);
            console.log("Role", role)

            await api.post(`/user/dossiers/${dossierRef}/add_stakeholder`, {
            partyRef,
            role: role ?? null
            });

            await loadDossierParties();
            setDialogOpen(false);

        } catch (err: any) {
            console.error(err);
            // Extract error message from API response
            const errorMessage = err.response?.data || "Erreur lors de l'ajout de la partie prenante";
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const loadRoles = async () => {
            try {
                  const res = await api.get<CodeDto[]>(`/user/dossiers/participant-roles`);
                  setRoles(res || []);
            } catch (err) {
                  console.error(err);
                  setError("Unable to load roles.");
            }
        };

        loadRoles();
    }, []);

    return {
        loading,
        error,

        dossierParties,
        availableParties,
        roles,

        dialogOpen,
        openDialog: () => {
          setDialogOpen(true);
          loadDialogData();
        },
        closeDialog: () => setDialogOpen(false),

        loadDossierParties,
        addStakeholder,
    };
}
