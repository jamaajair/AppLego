package org.spftech.backend.service;

import lombok.RequiredArgsConstructor;
import org.spftech.backend.entity.*;
import org.spftech.backend.repository.*;
import org.spftech.backend.entity.Party.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DossierRelatedPartyService {

    private final DossierRepository dossierRepository;
    private final PartyRepository partyRepository;
    private final DossierRelatedPartyRepository relatedPartyRepository;

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

    public List<Party> getPartiesOfDossier(String dossierRef) {
        return relatedPartyRepository.findByDossierRef(dossierRef)
                .stream()
                .map(DossierRelatedParty::getParty)
                .toList();
    }
}
