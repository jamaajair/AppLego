import React, { useEffect, useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Box,
  TextField,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  Alert,
} from "@mui/material";
import { AppButton } from "../../ui/AppButton";
import type { CodeDto } from "../../../types/config";
import type { DocumentAddFormValues } from "../../../types/documents/DocumentAddFormValues";

const DEFAULT_VALUES: DocumentAddFormValues = {
    documentTypeCode: "",
    documentStateCode: "INITIAL",
    documentRoleCode: "",
    localDocumentLabel: "",
    documentReferenceNotes: "",
    URI: "",
    CDIS: "",
    file: null
};

type DocumentAddDialogProps = {
  open: boolean;
  uploading: boolean;
  onClose: () => void;
  onSubmit: (values: DocumentAddFormValues, source: string) => Promise<void> | void;
  documentTypes: CodeDto[];
  documentStates: CodeDto[];
  documentRoles: CodeDto[];
};

export function DocumentAddDialog({ open, uploading, onClose, onSubmit, documentTypes, documentStates, documentRoles }: DocumentAddDialogProps) {
    const [source, setSource] = useState<string>('URI');
    const [loading, setLoading] = useState<boolean>(false);
    const [submitError, setSubmitError] = useState<string | null>(null);
    const setField = <K extends keyof DocumentAddFormValues>(key: K, v: DocumentAddFormValues[K]) => setValues((prev) => ({ ...prev, [key]: v }));
    const [values, setValues] = useState<DocumentAddFormValues>(DEFAULT_VALUES);

    const isFormValid = (): boolean => {
        if (!values.documentRoleCode || !values.documentStateCode || !values.documentTypeCode) return false;
        if (source === "URI") return values.URI.trim() !== "";
        if (source === "CDIS") return values.CDIS.trim() !== "";
        if (source === "LOCAL") return !!values.file;
        return false;
    };

    const handleSubmit = async () => {
        if(!isFormValid())
            return;

        try {
          setSubmitError(null);
          await onSubmit(values, source);
          onClose();
        } catch(err) {
          const message = err instanceof Error ? err.message : "Request failed";
          setSubmitError(message);
        }
    }

    useEffect(() => {
        if(!open) return;
        setSource("URI");
        setValues(DEFAULT_VALUES);
        setSubmitError(null);
    }, [open])

  return (
    <Dialog open={open} fullWidth maxWidth="sm">
      <DialogTitle>New Document</DialogTitle>

      <DialogContent dividers>
        <Box display="flex" flexDirection="column" gap={2}>

          {submitError && (
            <Alert severity="error">
              {submitError}
            </Alert>
          )}

          <FormControl fullWidth size="small">
            <InputLabel>Source</InputLabel>
            <Select
            label="Source"
            value={source}
            onChange={(e) => setSource(e.target.value)}
            >
              <MenuItem value="URI">URI</MenuItem>
              <MenuItem value="CDIS">CDIS</MenuItem>
              <MenuItem value="LOCAL">LOCAL</MenuItem>
            </Select>
          </FormControl>

          {source === "LOCAL" ? (
              <Box display="flex" flexDirection="column" gap={2}>
                <input
                type="file"
                accept=".pdf,.png,.jpg,.jpeg"
                onChange={(e) => setField("file", e.target.files ? e.target.files[0] : null)}
                />

                <TextField
                label="Labellé"
                value={values.localDocumentLabel}
                onChange={(e) => setField("localDocumentLabel", e.target.value)}
                fullWidth
                />
            </Box>
            ) : (
            <TextField
            label={source === "URI" ? "URI" : "CDIS"}
            value={source === "URI" ? values.URI : values.CDIS}
            onChange={(e) => source === "URI" ? setField("URI", e.target.value) : setField("CDIS", e.target.value)}
            fullWidth
            />
            )}

          <FormControl fullWidth size="small">
            <InputLabel>Document Type</InputLabel>
            <Select
            label="Document Type"
            value={values.documentTypeCode}
            onChange={(e) => setField("documentTypeCode", e.target.value)}
            >
                {documentTypes.map((type) => (
                    <MenuItem key={type.code} value={type.code}>
                    {type.labelFr}
                    </MenuItem>
                ))}
            </Select>
          </FormControl>

          <FormControl fullWidth size="small">
            <InputLabel>Document Role</InputLabel>
            <Select
              label="Document Role"
              value={values.documentRoleCode}
              onChange={(e) => setField("documentRoleCode", e.target.value)}
            >
              {documentRoles.map((role) => (
                <MenuItem key={role.code} value={role.code}>
                  {role.labelFr}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <TextField
            label="Notes"
            value={values.documentReferenceNotes}
            onChange={(e) => setField("documentReferenceNotes", e.target.value)}
            fullWidth
            multiline
            minRows={2}
            maxRows={10}
          />

        </Box>
      </DialogContent>

      <DialogActions>
        <AppButton intent="ghost" onClick={onClose} disabled={uploading}> Annuler </AppButton>
        <AppButton intent="primary" onClick={handleSubmit} loading={uploading} disabled={!isFormValid()}> Ajouter </AppButton>
      </DialogActions>
    </Dialog>
  );
}