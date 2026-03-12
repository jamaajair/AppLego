import { Box, TextField, MenuItem, Typography, Alert, Stack } from "@mui/material";
import type { GenericFormProps } from "../../types/genericForm.ts";

const GenericForm = ({
                         config,
                         values,
                         error,
                         handleChange,
                         handleSubmit,
                         readOnly,
                     }: GenericFormProps) => {
    if (error) return <Alert severity="error">{error}</Alert>;
    if (!config) return <Typography>No config.</Typography>;

    const fields = config.fields ?? [];
    const mid = Math.ceil(fields.length / 2);
    const left = fields.slice(0, mid);
    const right = fields.slice(mid);

    const renderField = (field: any) => {
        const value = values[field.name] ?? "";

        return (
            <TextField
                key={field.name}
                label={field.label}
                fullWidth
                disabled={readOnly}
                required={field.required}
                type={field.type === "date" ? "date" : "text"}
                value={field.type === "date" ? String(value).slice(0, 10) : value}
                onChange={handleChange(field.name)}
                select={field.type === "select"}
                slotProps={{
                    select: { multiple: field.multiple ?? false },
                }}
                InputLabelProps={field.type === "date" ? { shrink: true } : undefined}
                multiline={field.type === "textarea"}
                minRows={field.type === "textarea" ? 3 : undefined}
                maxRows={field.type === "textarea" ? 10 : undefined}
            >
                {field.options &&
                    Object.entries(field.options).map(([key, label]) => (
                        <MenuItem key={key} value={key}>
                            {label as any}
                        </MenuItem>
                    ))}
            </TextField>
        );
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ width: "100%" }}>
            <Typography variant="h5" gutterBottom>
                {config.title}
            </Typography>

            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: { xs: "1fr", md: "1fr 1fr" },
                    gap: 2,
                    alignItems: "start",
                }}
            >
                <Stack spacing={2}>
                    {left.map(renderField)}
                </Stack>

                <Stack spacing={2}>
                    {right.map(renderField)}
                </Stack>
            </Box>
        </Box>
    );
};

export default GenericForm;
