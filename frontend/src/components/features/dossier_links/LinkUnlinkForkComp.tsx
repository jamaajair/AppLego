import React, { useEffect, useMemo, useState } from "react";
import { Box, Stack, Typography, Alert, Divider } from "@mui/material";
import { api } from "../../../services/api.ts";

import type { LinkedDossier } from "../../../types/linkedDossier.ts";
import type { Dossier } from "../../../models/dossier.ts";
import type { Code } from "../../../types/code.ts";

import LinkDossierComp from "./LinkDossierComp.tsx";
import ForkDossierButton from "./ForkDossierButton.tsx";
import LinkedDossierGrid from "./LinkedDossierGrid.tsx";

interface LinkUnlinkForkProps {
    id: string;
}

const LinkUnlinkForkComp: React.FC<LinkUnlinkForkProps> = ({ id }) => {
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    const [dossier, setDossier] = useState<Dossier | null>(null);
    const [linkedDossiers, setLinkedDossiers] = useState<LinkedDossier[]>([]);
    const [dossiers, setDossiers] = useState<Dossier[]>([]);
    const [linkKindCodes, setLinkKindCodes] = useState<Code[]>([]);

    useEffect(() => {
        const fetchAll = async () => {
            setLoading(true);
            setError(null);

            try {
                const [resDossier, resLinked, resDossiers, resKinds] = await Promise.all([
                    api.get(`/user/dossier/${id}`),
                    api.get(`/user/linked_dossier/${id}`),
                    api.get("user/my_dossiers"),
                    api.get("user/code/LINK_KIND"),
                ]);

                setDossier(resDossier.data ?? null);
                setLinkedDossiers(resLinked.data ?? []);
                setDossiers(resDossiers.data ?? []);
                setLinkKindCodes(resKinds.data ?? []);
            } catch (err) {
                setError("Unable to fetch dossier details.");
            } finally {
                setLoading(false);
            }
        };

        void fetchAll();
    }, [id]);

    const titleRight = useMemo(() => {
        if (!dossier) return null;
        return (
            <ForkDossierButton
                dossier={dossier}
                onClickExtra={(newLinked) => setLinkedDossiers((prev) => [...prev, newLinked])}
            />
        );
    }, [dossier]);

    if (loading) return <Typography variant="body2">Chargement…</Typography>;
    if (error) return <Alert severity="error">{error}</Alert>;
    if (!dossier) return <Alert severity="warning">Aucun dossier trouvé.</Alert>;

    return (
        <Stack spacing={2} sx={{ width: "100%" }}>
            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", gap: 2 }}>
                <Box>
                    <Typography variant="h6">Liens & fork</Typography>
                    <Typography variant="body2" color="text.secondary">
                        Liez des dossiers enfants ou forkez ce dossier.
                    </Typography>
                </Box>
                {titleRight}
            </Box>

            <Divider />

            <Box>
                <Typography variant="subtitle1" sx={{ mb: 1 }}>
                    Créer un lien
                </Typography>
                <LinkDossierComp
                    linkKinds={linkKindCodes}
                    dossiers={dossiers}
                    parentDossierRefProp={dossier.ref}
                    existingLinks={linkedDossiers}
                    onClickExtra={(newLinked) => setLinkedDossiers((prev) => [...prev, newLinked])}
                />
            </Box>

            <Divider />

            <LinkedDossierGrid
                dossier={dossier}
                linkedDossiersProp={linkedDossiers}
                onClickExtra={(unlinkedRef) =>
                    setLinkedDossiers((prev) => prev.filter((ld) => ld.childDossier.ref !== unlinkedRef))
                }
            />
        </Stack>
    );
};

export default LinkUnlinkForkComp;
