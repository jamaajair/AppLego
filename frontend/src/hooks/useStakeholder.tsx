import { useState, useEffect } from 'react';
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
    const [roles, setRoles] = useState<string[]>([]);

    const [dialogOpen, setDialogOpen] = useState(false);

    const loadDossierParties = async () => {
        try {
            setLoading(true);
            const res = await api.get(`/user/dossiers/${dossierRef}/parties`);
            console.log(res)
            const formatted = res.map((p: any) => ({
                ref: p.partyRef,
                label: p.label,
                kind: p.kind ?? "Undefined",
                role: p.role ?? null
            }));

            setDossierParties(formatted);
            setError(null);
        } catch (err) {
            console.error(err);
            setError("Impossible de charger les parties du dossier");
        } finally {
            setLoading(false);
        }
    };

    const loadDialogData = async () => {
        try {
            setLoading(true);
            const partiesResponse = await api.get(`/user/dossiers/parties`);

            setAvailableParties(partiesResponse || []);
        } catch (err) {
            console.error(err);
            setError('Impossible de charger les données du dialog');
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

        } catch (err) {
            console.error(err);
            setError("Erreur lors de l'ajout de la partie prenante");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const loadRoles = async () => {
            try {
                  const res = await api.get(`/user/dossiers/participant-roles`);
                  setRoles(res || []);
                  console.log("ROLES: ", res);
            } catch (err) {
                  console.error(err);
                  setError("Impossible de charger les rôles");
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
