package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.LocalDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalDocumentRepository extends JpaRepository<LocalDocument, Long> {
    
}
