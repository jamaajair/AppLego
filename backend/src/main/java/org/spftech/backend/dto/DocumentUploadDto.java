package org.spftech.backend.dto;

import java.sql.Date;

public record DocumentUploadDto(
    String type,
    String Notes,
    String Label,
    String State,
    Date Published,
    String fileBase64,   
    String contentType
) {}
