package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentSetRepository extends JpaRepository<DocumentSet, Long> {
    
}
