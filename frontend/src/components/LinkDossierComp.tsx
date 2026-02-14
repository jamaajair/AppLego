import { Box, MenuItem, TextField, Select, Button, FormControl, InputLabel, Grid} from "@mui/material"
import React, { useEffect } from "react";
import type { Dossier } from "../types/dossier";
import { api } from "../services/api";
import type { LinkedDossier } from "../types/linkedDossier";
import type { Code } from "../types/code";

interface LinkDossierProp {
    linkKinds: Code[];
    dossiers: Dossier[];
    parentDossierRefProp?: String;
    onClickExtra?: (newLink : LinkedDossier) => void;
}

const LinkDossierComp: React.FC<LinkDossierProp> = ({linkKinds, dossiers, parentDossierRefProp, onClickExtra}) => {
    const [parentDossierRef, setParentDossierRef] = React.useState<String | undefined>(parentDossierRefProp);
    const [childDossierRef, setChildDossierRef] = React.useState<String>();
    const [sequenceNumber, setSequenceNumber] = React.useState<number>();
    const [selectedLinkKindCode, setSelectedLinkKindCode] = React.useState<String>();

    const postLinkRequest = async () => {
        if(!parentDossierRef || !childDossierRef || !sequenceNumber || !selectedLinkKindCode){
            console.error("All fields are required to create a link");
            return;
        }

        if(parentDossierRef === childDossierRef){
            console.error("Parent and Child Dossier cannot be the same");
            return;
        }

        try{
            const response = await api.post("user/dossier/link", {
                "parentDossierRef": parentDossierRef, 
                "childDossierRef": childDossierRef, 
                "sequenceNumber": sequenceNumber, 
                "linkKind": selectedLinkKindCode });
            
            onClickExtra!(response.data.data);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        if(parentDossierRefProp)
            setParentDossierRef(parentDossierRefProp);
    } , [parentDossierRefProp]);

    return (
    <Box>
        <Grid container spacing={4}>
            <Grid size={{xs: 4}}>
                <FormControl fullWidth>
                    <InputLabel id="parent-dossier-label">Parent Dossier</InputLabel>
                    <Select id="parent-dossier" labelId="parent-dossier-label" value={parentDossierRef} onChange={(event) => setParentDossierRef(event.target.value)} disabled={parentDossierRefProp !== undefined}>
                        {dossiers.map((dossier) => (
                            <MenuItem key={dossier.ref} value={dossier.ref}>
                                {dossier.ref} | {dossier.label}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Grid>
            <Grid size={{xs: 4}}>
                <FormControl fullWidth>
                    <InputLabel id="child-dossier-label">Child Dossier</InputLabel>
                    <Select id="child-dossier" labelId="child-dossier-label" value={childDossierRef} onChange={(event) => setChildDossierRef(event.target.value)}>
                        {dossiers.map((dossier) => (
                            <MenuItem key={dossier.ref} value={dossier.ref}>
                                {dossier.ref} | {dossier.label}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Grid>
            <Grid size={{xs: 2}}>
                <TextField id="sequence-number" label="Sequence Number" type="number" value={sequenceNumber} onChange={(event) => setSequenceNumber(Number(event.target.value))}/>
            </Grid>
            <Grid size={{xs: 2}}>
                <FormControl fullWidth>
                    <InputLabel id="link-kind-label">Link Kind</InputLabel>
                    <Select id="link-kind" labelId="link-kind-label" value={selectedLinkKindCode} onChange={(event) => setSelectedLinkKindCode(event.target.value)}>
                        {linkKinds.map((linkKind) => (
                            <MenuItem key={linkKind.code} value={linkKind.code} >
                                {linkKind.labelEn}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Grid>
        </Grid>
        <Button onClick={postLinkRequest}> Create Link </Button>
    </Box>
    );

}

export default LinkDossierComp;