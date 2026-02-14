package org.spftech.backend.repository;

import java.util.List;
import org.spftech.backend.entity.LinkedDossier;
import org.spftech.backend.entity.LinkedDossierId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedDossierRepository extends JpaRepository<LinkedDossier, LinkedDossierId> {
    List<LinkedDossier> findByParentDossierRef(String DossierRef);
}