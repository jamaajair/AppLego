package org.spftech.backend.repository;

import java.util.List;

import org.spftech.backend.entity.Party.*;
import org.spftech.backend.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DossierRelatedPartyRepository extends JpaRepository<DossierRelatedParty, Long> {
    List<DossierRelatedParty> findByDossierRef(String dossierRef);
    DossierRelatedParty findByDossierRefAndPartyRef(String dossierRef, Long partyRef);
}
