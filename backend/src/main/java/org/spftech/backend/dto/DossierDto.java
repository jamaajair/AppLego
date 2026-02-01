package org.spftech.backend.dto;

import java.util.Date;

public record DossierDto(
    String ref,
    String label,
    String comments,
    Date createdAt,
    Date startNoLaterThan,
    Date expectCompletion,
    CodeDto state,
    CodeDto type
) {}

