package org.spftech.backend.dto.Document;

public record AddDocumentToDossierDto(
    String documentTypeCode,
    String documentStateCode,
    String documentRoleCode,
    String localDocumentLabel,
    String documentReferenceNotes,
    String CDIS,
    String URI
) {}
