import React, { useState, useEffect } from 'react';
import GenericEdit from '../../components/ui/GenericEdit';
import { useParams } from 'react-router-dom';
import AddStakeholder from "../../components/AddStakeholder";

const Dossier: React.FC = () => {
    const { id } = useParams();
    console.log("Dossier ", id);

    return (
        <div>
            <GenericEdit configPath="/config/dossier.json" dataUrl={`/user/dossier/${id}`} submitUrl={`/user/dossier/${id}`}
            />
            <AddStakeholder dossierRef={id} />
        </div>
    )
};

export default Dossier;