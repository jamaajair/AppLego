package org.spftech.backend.dto;

public record DocumentReferenceDto(
    String notes,
    String id_doc_cdis,
    String id_doc_uri,
    Short is_doc_local
) {}
