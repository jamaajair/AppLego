import {IconButton} from "@mui/material"
import AddBoxIcon from '@mui/icons-material/AddBox';
import React from "react";
import type { Dossier } from "../types/dossier";
import { api } from "../services/api";
import type { LinkedDossier } from "../types/linkedDossier";

interface ForkDossierProp {
    dossier: Dossier;
    onClickExtra?: (newLink : LinkedDossier) => void;
}

const ForkDossierButton: React.FC<ForkDossierProp> = ({dossier, onClickExtra}) => {

    const postForkRequest = async () => {
        try{
            const dossierRef = dossier.ref;
            const response = await api.post(`user/dossier/${dossierRef}/fork`, {});
            const linkedDossier = response.data.data;
            onClickExtra!(linkedDossier);
        } catch (err) {
            console.log(err);
        }
    };

    return (
        <IconButton onClick={(event) => {event.stopPropagation(); postForkRequest();}} > 
            <AddBoxIcon />
        </IconButton>
    );

}

export default ForkDossierButton;