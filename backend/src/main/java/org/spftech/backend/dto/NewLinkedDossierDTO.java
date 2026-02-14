package org.spftech.backend.dto;

public record NewLinkedDossierDTO(
    String parentDossierRef,
    String childDossierRef,
    Integer sequenceNumber,
    String linkKind
) {}