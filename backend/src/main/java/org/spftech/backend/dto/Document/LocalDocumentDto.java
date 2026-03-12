package org.spftech.backend.dto.Document;

import java.sql.Date;

public record LocalDocumentDto(
    Long ref,
    String label,           
    String typeCode,        
    String stateCode,      
    Date publishedDate,
    Short archivedAsDocRef,
    DocumentReferenceDto reference
) {}