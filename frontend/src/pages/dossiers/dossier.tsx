import React from 'react';
import GenericEdit from '../../components/ui/GenericEdit';
import { useParams } from 'react-router-dom';
import AddStakeholder from "../../components/AddStakeholder";
import LinkUnlinkForkComp from '../../components/LinkUnlinkForkComp';
import { Typography } from '@mui/material';
import {Box} from '@mui/material';

import  AddDocument  from '../../components/AddDocument';

const Dossier: React.FC = () => {
    const { id } = useParams();
    console.log("Dossier ", id);

    if(!id) return <Typography>Missing Parameter </Typography>;

    return (
        <Box sx={{overflow: 'auto', height: '400'}}>
            <GenericEdit configPath="/config/dossier.json" dataUrl={`/user/dossier/${id}`} submitUrl={`/user/dossier/${id}`}
            />

            {/* <AddStakeholder dossierRef={id} /> */}
            <AddDocument  dossierRef={id} />

            <LinkUnlinkForkComp id={id} />
        </Box>
    )
};

export default Dossier;