package org.spftech.backend.repository;

import java.util.List;
import java.util.Optional;

import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.entity.Document.DocumentRelatedSetId;
import org.spftech.backend.entity.Document.DocumentSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRelatedSetRepository extends JpaRepository<DocumentRelatedSet, DocumentRelatedSetId> {
    Page<DocumentRelatedSet> findByInSet(DocumentSet inSet, Pageable pageable);
    Long countByInSet(DocumentSet inSet);
    List<DocumentRelatedSet> findByDocument(DocumentReference document);
    Optional<DocumentRelatedSet> findByInSetAndDocument(DocumentSet inSet, DocumentReference document);
}
