import useGenericEdit from "../../hooks/useGenericEdit";
import GenericForm from "./GenericForm";
import { useState } from "react";
import { Button, Stack, Box } from "@mui/material";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";

interface GenericEditProps {
    submitUrl: string;
    configPath?: string | null;
    dataUrl?: string | null;
}

export default function GenericEdit({ configPath, dataUrl, submitUrl }: GenericEditProps) {
    const form = useGenericEdit(submitUrl, configPath, dataUrl);
    const [editing, setEditing] = useState(false);

    const canEdit = Boolean(submitUrl);

    return (
        <Stack spacing={3}>
            <GenericForm {...form} readOnly={!editing || !canEdit} />

            {canEdit && (
                <Box sx={{ display: "flex", justifyContent: "flex-end" }}>
                    {!editing ? (
                        <Button
                            variant="outlined"
                            size="small"
                            startIcon={<EditOutlinedIcon />}
                            onClick={() => setEditing(true)}
                        >
                            Modifier
                        </Button>
                    ) : (
                        <Stack direction="row" spacing={1}>
                            <Button
                                variant="text"
                                size="small"
                                onClick={() => {
                                    form.handleReset();
                                    setEditing(false);
                                }}
                            >
                                Annuler
                            </Button>

                            <Button
                                variant="contained"
                                size="small"
                                type="submit"
                                onClick={(e) => form.handleSubmit(e as any)}
                            >
                                Enregistrer
                            </Button>
                        </Stack>
                    )}
                </Box>
            )}
        </Stack>
    );
}
