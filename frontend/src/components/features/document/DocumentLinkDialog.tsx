import React, { useEffect, useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
} from "@mui/material";
import { AppButton } from "../../ui/AppButton";
import type { CodeDto } from "../../../types/config";
import type { Dossier } from "../../../types/dossier"; // adjust import path to where your Dossier interface lives

type Props = {
  open: boolean;
  loading: boolean;
  onClose: () => void;
  dossiers: Dossier[];
  documentRoles: CodeDto[];
  excludeDossierId?: string;
  onSubmit: (otherDossierId: string, documentRoleCode: string) => Promise<void>;
};

export default function DocumentLinkDialog({
  open,
  loading,
  onClose,
  dossiers,
  documentRoles,
  excludeDossierId,
  onSubmit,
}: Props) {
  const [selectedDossierRef, setSelectedDossierRef] = useState<string>("");
  const [selectedDocumentRoleCode, setSelectedDocumentRoleCode] = useState<string>("");
  const [submitError, setSubmitError] = useState<string | null>(null);

  const filteredDossiers = excludeDossierId
    ? dossiers.filter((d) => d.ref !== excludeDossierId)
    : dossiers;

  const isValid = selectedDossierRef.trim() !== "" && selectedDocumentRoleCode.trim() !== "";

  const handleSubmit = async () => {
    if (!isValid) return;
    try {
      setSubmitError(null);
      await onSubmit(selectedDossierRef, selectedDocumentRoleCode);
      onClose();
    } catch (err) {
      setSubmitError(err instanceof Error ? err.message : "Request failed");
    }
  };

  useEffect(() => {
    if (!open) return;
    setSubmitError(null);
    setSelectedDocumentRoleCode("");
    setSelectedDossierRef("");
  }, [open, documentRoles]);

  return (
    <Dialog open={open} fullWidth maxWidth="sm">
      <DialogTitle>Lier le document à un autre dossier</DialogTitle>

      <DialogContent dividers>
        <Box display="flex" flexDirection="column" gap={2}>
          {submitError && <Alert severity="error">{submitError}</Alert>}

          <FormControl fullWidth size="small">
            <InputLabel>Dossier</InputLabel>
            <Select
              label="Dossier"
              value={selectedDossierRef}
              onChange={(e) => setSelectedDossierRef(e.target.value)}
            >
              {filteredDossiers.map((d) => (
                <MenuItem key={d.ref} value={d.ref}>
                  {d.ref} — {d.label}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl fullWidth size="small">
            <InputLabel>Role</InputLabel>
            <Select
              label="Role"
              value={selectedDocumentRoleCode}
              onChange={(e) => setSelectedDocumentRoleCode(e.target.value)}
            >
              {documentRoles.map((r) => (
                <MenuItem key={r.code} value={r.code}>
                  {r.labelFr}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Box>
      </DialogContent>

      <DialogActions>
        <AppButton intent="ghost" onClick={onClose} disabled={loading}>
          Annuler
        </AppButton>
        <AppButton intent="primary" onClick={handleSubmit} loading={loading} disabled={!isValid}>
          Lier
        </AppButton>
      </DialogActions>
    </Dialog>
  );
}