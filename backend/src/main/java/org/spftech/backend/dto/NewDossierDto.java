package org.spftech.backend.dto;

import java.util.Date;

public record NewDossierDto(
    String ref,
    String label,
    String comments,
    String type,
    Date startNoLaterThan,
    Date expectCompletion
) {}

