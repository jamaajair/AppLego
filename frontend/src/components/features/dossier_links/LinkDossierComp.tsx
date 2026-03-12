import React, { useEffect, useMemo, useState } from "react";
import { Box, MenuItem, TextField, Button, FormControl, InputLabel, Select, Alert, Stack } from "@mui/material";
import type { Dossier } from "../../../types/dossier.ts";
import { api } from "../../../services/api.ts";
import type { LinkedDossier } from "../../../types/linkedDossier.ts";
import type { Code } from "../../../types/code.ts";
import AddLinkRoundedIcon from "@mui/icons-material/AddLinkRounded";
import {useNavigate} from "react-router-dom";

interface LinkDossierProp {
    linkKinds: Code[];
    dossiers: Dossier[];
    parentDossierRefProp?: string;
    existingLinks?: LinkedDossier[];
    onClickExtra?: (newLink: LinkedDossier) => void;
}

const LinkDossierComp: React.FC<LinkDossierProp> = ({
                                                        linkKinds,
                                                        dossiers,
                                                        parentDossierRefProp,
                                                        existingLinks,
                                                        onClickExtra,
                                                    }) => {
    const [parentDossierRef, setParentDossierRef] = useState<string>(parentDossierRefProp ?? "");
    const [childDossierRef, setChildDossierRef] = useState<string>("");
    const [sequenceNumber, setSequenceNumber] = useState<number | "">("");
    const [selectedLinkKindCode, setSelectedLinkKindCode] = useState<string>("");

    const [submitting, setSubmitting] = useState(false);
    const [formError, setFormError] = useState<string | null>(null);

    useEffect(() => {
        if (parentDossierRefProp) setParentDossierRef(parentDossierRefProp);
    }, [parentDossierRefProp]);

    const parentItems = useMemo(
        () =>
            dossiers.map((d) => ({
                value: d.ref,
                label: `${d.ref} — ${d.label ?? ""}`,
            })),
        [dossiers]
    );

    const forbiddenChildRefs = useMemo(() => {
        const s = new Set<string>();
        if (parentDossierRef) s.add(parentDossierRef);
        (existingLinks ?? []).forEach((l) => s.add(l.childDossier.ref));
        return s;
    }, [parentDossierRef, existingLinks]);

    const childItems = useMemo(
        () =>
            dossiers
                .filter((d) => !forbiddenChildRefs.has(d.ref))
                .map((d) => ({
                    value: d.ref,
                    label: `${d.ref} — ${d.label ?? ""}`,
                })),
        [dossiers, forbiddenChildRefs]
    );

    useEffect(() => {
        if (childDossierRef && forbiddenChildRefs.has(childDossierRef)) setChildDossierRef("");
    }, [childDossierRef, forbiddenChildRefs]);

    const validate = () => {
        if (!parentDossierRef || !childDossierRef || !sequenceNumber || !selectedLinkKindCode) {
            return "Tous les champs sont requis.";
        }
        if (parentDossierRef === childDossierRef) {
            return "Le dossier parent et enfant ne peuvent pas être identiques.";
        }
        if ((existingLinks ?? []).some((l) => l.childDossier.ref === childDossierRef)) {
            return "Ce dossier est déjà lié.";
        }
        return null;
    };

    const postLinkRequest = async () => {
        const v = validate();
        if (v) {
            setFormError(v);
            return;
        }

        setFormError(null);
        setSubmitting(true);
        try {
            const response = await api.post("user/dossier/link", {
                parentDossierRef,
                childDossierRef,
                sequenceNumber,
                linkKind: selectedLinkKindCode,
            });

            const created = response?.data?.data as LinkedDossier | undefined;
            if (created && onClickExtra) onClickExtra(created);

            if (!parentDossierRefProp) setParentDossierRef("");
            setChildDossierRef("");
            setSequenceNumber("");
            setSelectedLinkKindCode("");

        } catch (err) {
            console.log(err);
            setFormError("Impossible de créer le lien.");
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Box component="form" onSubmit={(e) => { e.preventDefault(); void postLinkRequest(); }}>
            <Stack spacing={2}>
                {formError && <Alert severity="error">{formError}</Alert>}

                <Box
                    sx={{
                        display: "grid",
                        gridTemplateColumns: { xs: "1fr", md: "1fr 1fr" },
                        gap: 2,
                        alignItems: "start",
                    }}
                >
                    <FormControl fullWidth>
                        <InputLabel id="parent-dossier-label">Parent</InputLabel>
                        <Select
                            id="parent-dossier"
                            labelId="parent-dossier-label"
                            label="Parent"
                            value={parentDossierRef}
                            onChange={(event) => setParentDossierRef(event.target.value)}
                            disabled={Boolean(parentDossierRefProp)}
                        >
                            {parentItems.map((d) => (
                                <MenuItem key={d.value} value={d.value}>
                                    {d.label}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <FormControl fullWidth>
                        <InputLabel id="child-dossier-label">Enfant</InputLabel>
                        <Select
                            id="child-dossier"
                            labelId="child-dossier-label"
                            label="Enfant"
                            value={childDossierRef}
                            onChange={(event) => setChildDossierRef(event.target.value)}
                        >
                            {childItems.map((d) => (
                                <MenuItem key={d.value} value={d.value}>
                                    {d.label}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <TextField
                        fullWidth
                        label="Séquence"
                        type="number"
                        value={sequenceNumber}
                        onChange={(event) => {
                            const v = event.target.value;
                            setSequenceNumber(v === "" ? "" : Number(v));
                        }}
                        inputProps={{ min: 1 }}
                    />

                    <FormControl fullWidth>
                        <InputLabel id="link-kind-label">Type</InputLabel>
                        <Select
                            id="link-kind"
                            labelId="link-kind-label"
                            label="Type"
                            value={selectedLinkKindCode}
                            onChange={(event) => setSelectedLinkKindCode(event.target.value)}
                        >
                            {linkKinds.map((k) => (
                                <MenuItem key={k.code} value={k.code}>
                                    {k.labelEn}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Box>

                <Box sx={{ display: "flex", justifyContent: "flex-end" }}>
                    <Button
                        type="submit"
                        variant="contained"
                        startIcon={<AddLinkRoundedIcon />}
                        disabled={submitting}
                        sx={{ minWidth: 160 }}
                    >
                        Lier
                    </Button>
                </Box>
            </Stack>
        </Box>
    );
};

export default LinkDossierComp;
