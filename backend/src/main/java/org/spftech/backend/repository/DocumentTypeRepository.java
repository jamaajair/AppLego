package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {    
}
