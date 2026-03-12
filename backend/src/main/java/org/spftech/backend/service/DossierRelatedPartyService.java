package org.spftech.backend.service;

import lombok.RequiredArgsConstructor;
import org.spftech.backend.entity.*;
import org.spftech.backend.entity.Party.*;
import org.spftech.backend.ruleunit.ValidationContext;
import org.spftech.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DossierRelatedPartyService {

    private final DossierRepository dossierRepository;
    private final PartyRepository partyRepository;
    private final DossierRelatedPartyRepository relatedPartyRepository;
    private final CodeRepository codeRepository;
    private final ParticipantRoleRepository participantRoleRepository;

    public DossierRelatedParty linkPartyToDossier(String dossierRef, Long partyRef) {
        Dossier dossier = dossierRepository.findById(dossierRef)
                .orElseThrow(() -> new RuntimeException("Dossier non trouvé"));
        Party party = partyRepository.findById(partyRef)
                .orElseThrow(() -> new RuntimeException("Partie non trouvée"));

        DossierRelatedParty relation = new DossierRelatedParty();
        relation.setDossier(dossier);
        relation.setParty(party);

        return relatedPartyRepository.save(relation);
    }


    @Transactional
    public DossierRelatedParty linkPartyToDossierWithRules(String dossierRef, Long partyRef, String roleCodeValue, List<String> groups) {
        // Load entities
        Dossier dossier = dossierRepository.findById(dossierRef)
                .orElseThrow(() -> new RuntimeException("Dossier non trouvé"));
        Party party = partyRepository.findById(partyRef)
                .orElseThrow(() -> new RuntimeException("Partie non trouvée"));

        DossierRelatedParty existingRelation = relatedPartyRepository.findByDossierRefAndPartyRef(dossierRef, partyRef);
        if (existingRelation != null) {
            throw new RuntimeException("Relationship already exists");
        }

        ParticipantRole role = null;
        if (roleCodeValue != null) {
            role = codeRepository.findByCodeValueAndUsageContext(roleCodeValue, "PARTICIPANT_ROLE")
                    .stream()
                    .findFirst()
                    .map(code -> {
                        ParticipantRole newRole = new ParticipantRole(code);
                        return participantRoleRepository.save(newRole);
                    })
                    .orElseThrow(() -> new RuntimeException("Participant role not found"));
        }

        ValidationContext context = new ValidationContext().with(dossier);
        if (role != null) {
            context = context.with(role);
        }
        context.fireGroups(groups);

        DossierRelatedParty relation = new DossierRelatedParty();
        relation.setDossier(dossier);
        relation.setParty(party);
        relation.setParticipantRole(role);

        return relatedPartyRepository.save(relation);
    }

    public List<Party> getPartiesOfDossier(String dossierRef) {
        return relatedPartyRepository.findByDossierRef(dossierRef)
                .stream()
                .map(DossierRelatedParty::getParty)
                .toList();
    }
}
