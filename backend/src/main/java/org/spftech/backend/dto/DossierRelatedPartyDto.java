package org.spftech.backend.dto;
import org.spftech.backend.entity.Party.PartyKind;
import org.spftech.backend.dto.CodeDto;

public record DossierRelatedPartyDto(
        Long partyRef,
        String label,
        PartyKind kind,
        CodeDto role
) {}