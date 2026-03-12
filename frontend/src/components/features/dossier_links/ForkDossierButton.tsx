import React, { useState } from "react";
import { Button } from "@mui/material";
import ContentCopyRoundedIcon from "@mui/icons-material/ContentCopyRounded";
import type { Dossier } from "../../../types/dossier.ts";
import { api } from "../../../services/api.ts";
import type { LinkedDossier } from "../../../types/linkedDossier.ts";

interface ForkDossierProp {
    dossier: Dossier;
    onClickExtra?: (newLink: LinkedDossier) => void;
    size?: "small" | "medium";
}

const ForkDossierButton: React.FC<ForkDossierProp> = ({ dossier, onClickExtra, size = "small" }) => {
    const [loading, setLoading] = useState(false);

    const postForkRequest = async () => {
        try {
            setLoading(true);
            const dossierRef = dossier.ref;
            const response = await api.post(`user/dossier/${dossierRef}/fork`, {});
            const linkedDossier = response?.data?.data;
            if (linkedDossier && onClickExtra) onClickExtra(linkedDossier);
        } catch (err) {
            console.log(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Button
            variant="outlined"
            size={size}
            startIcon={<ContentCopyRoundedIcon />}
            onClick={(e) => {
                e.stopPropagation();
                void postForkRequest();
            }}
            disabled={loading}
        >
            Fork
        </Button>
    );
};

export default ForkDossierButton;
