package org.spftech.backend.repository;

import org.spftech.backend.entity.DossierRelatedCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.transaction.Transactional;

public interface DossierRelatedCollaboratorRepository extends JpaRepository<DossierRelatedCollaborator, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE dossier_related_collaborator AUTO_INCREMENT=1", nativeQuery = true)
    void resetAutoIncr();
}