import React from "react";
import { Box, TextField, MenuItem, Button, Typography, Alert } from "@mui/material";

const GenericForm = ({
    config,
    values,
    error,
    submitting,
    handleChange,
    handleSubmit,
    handleReset,
    readOnly
}) => {

    if (error) return <Alert severity="error">{error}</Alert>;
    if (!config) return <Typography>No config.</Typography>;

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ maxWidth: 800, mx: "auto", mt: 3 }}>
            <Typography variant="h5" gutterBottom>
                {config.title}
            </Typography>

            {config.fields.map(field => {
                const value = values[field.name] ?? "";

                return (
                    <TextField
                        key={field.name}
                        label={field.label}
                        fullWidth
                        disabled={readOnly}
                        required={field.required}
                        type={field.type === "date" ? "date" : "text"}
                        value={field.type === "date" ? String(value).slice(0,10) : value}
                        onChange={handleChange(field.name)}
                        sx={{ mb: 2 }}
                        select={field.type === "select"}
                        SelectProps={{
                        multiple: field.multiple ?? false
                        }}
                        InputLabelProps={
                        field.type === "date"
                        ? { shrink: true }
                        : undefined
                        }

                        multiline={field.type === "textarea"}
                        minRows={field.type === "textarea" ? 3 : undefined}
                        maxRows={field.type === "textarea" ? 10 : undefined}
                    >

                        {field.options &&
                            Object.entries(field.options).map(([key, label]) => (
                                <MenuItem key={key} value={key}>{label}</MenuItem>
                            ))}
                    </TextField>
                );
            })}

            {!readOnly && (
                <Box sx={{ display: "flex", gap: 2 }}>
                    <Button type="submit" variant="contained">
                        {submitting ? "Envoi..." : "Sauver"}
                    </Button>
                    <Button type="button" variant="outlined" onClick={handleReset}>
                        Reset
                    </Button>
                </Box>
            )}
        </Box>
    );
};

export default GenericForm;