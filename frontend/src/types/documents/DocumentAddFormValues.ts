export type DocumentAddFormValues = {
  documentTypeCode: string;
  documentStateCode: string;
  documentRoleCode: string;
  localDocumentLabel: string;
  documentReferenceNotes: string;
  URI: string;
  CDIS: string;
  file?: File | null;
};