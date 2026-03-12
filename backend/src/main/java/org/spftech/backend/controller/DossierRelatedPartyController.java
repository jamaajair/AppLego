package org.spftech.backend.controller;

import java.util.List;

import org.spftech.backend.dto.*;
import org.spftech.backend.entity.*;
import org.spftech.backend.entity.Party.*;
import org.spftech.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.spftech.backend.service.CodeMapper;
import org.spftech.backend.service.DossierRelatedPartyService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/user/dossiers")
@PreAuthorize("hasAnyRole('EMPLOYEES', 'ADMIN', 'SUPERVISORS')")
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

    @Autowired
    private DossierRelatedPartyService dossierRelatedPartyService;

    @PostMapping("/{dossierRef}/add_stakeholder")
    public ResponseEntity<String> addStakeholder(
            @PathVariable String dossierRef,
            @RequestBody AddStakeholderDto dto
    ) {

        Long partyRef = dto.partyRef();
        if (partyRef == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No party assigned.\n");
        }

        try {
            List<String> validationGroups = List.of("valid-dossier-state");
            if (dto.role() != null) {
                validationGroups = List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp");
            }
            
            dossierRelatedPartyService.linkPartyToDossierWithRules(
                dossierRef,
                partyRef,
                dto.role(),
                validationGroups
            );

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stakeholder added successfully.\n");
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message != null && (message.contains("Dossier non trouvé") || message.contains("Partie non trouvée"))) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: " + message + "\n");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + message + "\n");
        }
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

    @DeleteMapping("/{dossierRef}/stakeholders/{partyRef}")
    public ResponseEntity<Void> deleteStakeholder(
            @PathVariable String dossierRef,
            @PathVariable Long partyRef
    ) {
        Dossier dossier = dossierRepository.findById(dossierRef).orElse(null);
        if (dossier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Party party = partyRepository.findById(partyRef).orElse(null);
        if (party == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DossierRelatedParty relation = relatedPartyRepository.findByDossierRefAndPartyRef(dossierRef, partyRef);
        if (relation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ParticipantRole role = relation.getParticipantRole();

        relatedPartyRepository.delete(relation);

        if (role != null) {
            participantRoleRepository.delete(role);
        }

        return ResponseEntity.noContent().build();
    }
}
