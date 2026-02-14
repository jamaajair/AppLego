package org.spftech.backend.dto;

public record LinkedDossierDTO(
    DossierDto parentDossier,
    DossierDto childDossier,
    Integer sequenceNumber,
    LinkKindDTO linkKind
) {}