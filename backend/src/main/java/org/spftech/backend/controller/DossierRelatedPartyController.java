package org.spftech.backend.controller;

import java.util.List;

import org.spftech.backend.repository.*;
import org.spftech.backend.dto.*;
import org.spftech.backend.entity.*;
import org.spftech.backend.entity.Party.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.spftech.backend.service.CodeMapper;

@RestController
@RequestMapping("/api/user/dossiers")
public class DossierRelatedPartyController {
    CodeMapper codeMapper = new CodeMapper();

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private DossierRelatedPartyRepository relatedPartyRepository;

    @Autowired
    private ParticipantRoleRepository participantRoleRepository;

    @Autowired
    private CodeRepository codeRepository;

    @PostMapping("/{dossierRef}/add_stakeholder")
    public ResponseEntity<String> addStakeholder(
            @PathVariable String dossierRef,
            @RequestBody AddStakeholderDto dto
    ) {

        Dossier dossier = dossierRepository.findById(dossierRef)
                .orElse(null);

        if (dossier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Dossier not found.\n");
        }

        Long partyRef = dto.partyRef();
        if (partyRef == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No party assigned.\n");
        }

        Party party = partyRepository.findById(partyRef)
                .orElse(null);

        if (party == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Party not found.\n");
        }

        if(relatedPartyRepository.findByDossierRefAndPartyRef(dossierRef, partyRef) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Stakeholder already linked to dossier.\n");
        }

        ParticipantRole role = new ParticipantRole();
        if(dto.role() != null) {
            Code code = codeRepository.findByCodeValueAndUsageContext(dto.role(), "PARTICIPANT_ROLE")
                    .stream().findFirst().orElse(null);
            role.setCode(code);
        }


        DossierRelatedParty relation =
                relatedPartyRepository.findByDossierRefAndPartyRef(dossierRef, partyRef);

        if (relation == null) {
            relation = new DossierRelatedParty();
            relation.setDossier(dossier);
            relation.setParty(party);
            relation.setParticipantRole(role);
        }

        participantRoleRepository.save(role);
        relatedPartyRepository.save(relation);

        System.out.println("Added stakeholder: Dossier " + dossierRef + " - Party " + partyRef + "Role: " + relation.getParticipantRole());

        return ResponseEntity.status(HttpStatus.OK)
                .body("Stakeholder added successfully.\n");
    }

    @GetMapping("/{dossierId}/parties")
    public List<DossierRelatedPartyDto> getPartiesOfDossier(@PathVariable String dossierId) {
        return relatedPartyRepository.findByDossierRef(dossierId)
                .stream()
                .map(r -> new DossierRelatedPartyDto(
                        r.getParty().getRef(),
                        r.getParty().getLabel(),
                        r.getParty().getKind(),
                        r.getParticipantRole() != null ? codeMapper.toDto(r.getParticipantRole().getCode()) : null
                ))
                .toList();
    }
}