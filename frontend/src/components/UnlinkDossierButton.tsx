import {IconButton, type SxProps, type Theme} from "@mui/material"
import CancelIcon from '@mui/icons-material/Cancel';
import { useEffect , useState} from "react";
import type { Dossier } from "../types/dossier";
import { api } from "../services/api";

interface UninkDossierProp {
    parentDossierProp: Dossier;
    childDossier: Dossier;
    sx?: SxProps<Theme>; 
    onUnlink?: () => void;
}

const UnlinkDossierButton: React.FC<UninkDossierProp> = ({parentDossierProp, childDossier, sx, onUnlink}) => {
    const [parentDossier, setParentDossier] = useState<Dossier>(parentDossierProp);

    const postUnlinkRequest = async () => {
        try{
            const response = await api.delete("user/dossier/unlink", {
                "parentDossierRef": parentDossier.ref, 
                "childDossierRef": childDossier.ref 
            });

            onUnlink?.(); //only called if delete was successful
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        console.log(parentDossierProp);
        if(parentDossierProp)
            setParentDossier(parentDossierProp);
    }, [parentDossierProp]);

    return (
        <IconButton onClick={(event) => {event.stopPropagation(); postUnlinkRequest();}} sx={{width: '100%', height: '100%', ...sx}} color="error"> 
            <CancelIcon />
        </IconButton>
    );

}

export default UnlinkDossierButton;