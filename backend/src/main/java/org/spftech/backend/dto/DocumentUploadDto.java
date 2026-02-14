package org.spftech.backend.dto;

public record DocumentUploadDto(
    String dossierRef,
    String documentTypeCode,
    String documentStateCode,
    String localDocumentLabel,
    String CDISorURIorLOCAL,
    String CDIS,
    String URI
) {}
