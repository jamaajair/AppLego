package org.spftech.backend.dto.Document;

public record DocumentListItemDto(
    // DocumentRelatedSet
    Integer rankInSet,
    String roleInSetCode,   //to DocumentRole

    // DocumentReference,
    Long    ref,
    String  typeCode,   //to DocumentType
    String  notes,
    Short   isDocLocal,

    // LocalDocument
    String label,
    String stateCode    //to DocumentState
) {}