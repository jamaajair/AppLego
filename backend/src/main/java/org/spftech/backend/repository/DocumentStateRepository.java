package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentStateRepository extends JpaRepository<DocumentState, String> {
    
}
