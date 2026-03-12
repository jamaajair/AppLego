import useGenericNew from "../../hooks/useGenericNew";
import GenericForm from "./GenericForm";
import { Button, Stack, Box } from "@mui/material";
import AddOutlinedIcon from "@mui/icons-material/AddOutlined";
import { useNavigate } from "react-router-dom";
import type {GenericNewProps} from "../../types/genericNewProps.ts";


export default function GenericNew({ configPath, submitUrl, cancelTo, submitLabel, onSuccessNavigateTo }: GenericNewProps) {
    const form = useGenericNew(configPath, submitUrl, {onSuccessNavigateTo});
    const navigate = useNavigate();

    const canSubmit = Boolean(submitUrl);

    return (
        <Stack spacing={3} marginX={2}>
            <GenericForm {...form} readOnly={false} />

            {canSubmit && (
                <Box sx={{ display: "flex", justifyContent: "flex-end" }}>
                    <Stack direction="row" spacing={1}>
                        <Button
                            variant="text"
                            size="small"
                            onClick={() => {
                                if (cancelTo) navigate(cancelTo);
                                else navigate(-1);
                            }}
                        >
                            Annuler
                        </Button>

                        <Button
                            variant="contained"
                            size="small"
                            startIcon={<AddOutlinedIcon />}
                            type="submit"
                            onClick={(e) => form.handleSubmit(e as any)}
                            disabled={form.submitting}
                        >
                            {submitLabel ?? "Créer"}
                        </Button>
                    </Stack>
                </Box>
            )}
        </Stack>
    );
}