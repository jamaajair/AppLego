import type { LinkedDossier } from "../types/linkedDossier";
import type { Dossier } from "../models/dossier";
import type { Code } from "../types/code";

import { useEffect, useState } from "react";
import { Box, Typography} from "@mui/material";
import { api } from "../services/api";
import LinkDossierComp from "./LinkDossierComp";
import ForkDossierButton from "./ForkDossierButton";
import LinkedDossierGrid from "./LinkedDossierGrid";

interface LinkUnlinkForkProps {
    id: string;
}

const LinkUnlinkForkComp: React.FC<LinkUnlinkForkProps> = ({id}) => {
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const [dossier, setDossier] = useState<Dossier>();
    const [linkedDossiers, setLinkedDossiers] = useState<LinkedDossier[]>([]);
    const [dossiers, setDossiers] = useState<Dossier[]>([]);
    const [linkKindCodes, setLinkKindCodes] = useState<Code[]>([]);

    useEffect(() => {
        const fetchDossier = async () => {
            try {
                const response = await api.get(`/user/dossier/${id}`);
                setDossier(response.data || null);

                const responseLinkedDossier = await api.get(`/user/linked_dossier/${id}`);
                setLinkedDossiers(responseLinkedDossier.data || null);

                const responseDossiers = await api.get("user/my_dossiers");
                setDossiers(responseDossiers.data);

                const responseLinkKinds = await api.get("user/code/LINK_KIND");
                setLinkKindCodes(responseLinkKinds.data);

            } catch (err) {
                setError('Unable to fetch dossier details: '+err);
            } finally {
                setLoading(false);
            }
        };
        fetchDossier();
    }, [id]);

    if (loading) return <Typography>Chargement du dossier...</Typography>;
    if (error) return <Typography color="error">Erreur : {error}</Typography>;
    if (!dossier) return <Typography>Aucun dossier trouvé.</Typography>;

    return (   
        <Box>
            <LinkDossierComp linkKinds={linkKindCodes} dossiers={dossiers} parentDossierRefProp={dossier.ref} onClickExtra={(newLinkedDossier : LinkedDossier) => setLinkedDossiers([...linkedDossiers, newLinkedDossier])}/>

            <ForkDossierButton dossier={dossier} onClickExtra={(newLinkedDossier : LinkedDossier) => {setLinkedDossiers([...linkedDossiers, newLinkedDossier]);} }/>

            <LinkedDossierGrid dossier={dossier} linkedDossiersProp={linkedDossiers} onClickExtra={(unlinkedDossierRef : String) => setLinkedDossiers(prev => prev.filter(linkedDossier => linkedDossier.childDossier.ref !== unlinkedDossierRef))}/>
        </Box>
    );
};

export default LinkUnlinkForkComp;