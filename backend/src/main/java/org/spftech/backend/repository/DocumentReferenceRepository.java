package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.LocalDocument;
import org.spftech.backend.entity.Document.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface DocumentReferenceRepository extends JpaRepository<DocumentReference, Long> {
}