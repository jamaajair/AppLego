package org.spftech.backend.repository;

import java.util.Optional;

import org.spftech.backend.entity.Document.DocumentSet;
import org.spftech.backend.entity.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentSetRepository extends JpaRepository<DocumentSet, Long> {
    Optional<DocumentSet> findByBoundToDossier(Dossier dossier);
}
