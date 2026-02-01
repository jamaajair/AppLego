package org.spftech.backend.dto;

import java.util.Date;

public record PatchDossierDto(
        String ref,
        String label,
        String comments,
        Date createdAt,
        Date startNoLaterThan,
        Date expectCompletion,
        String state,
        String type
) {}

