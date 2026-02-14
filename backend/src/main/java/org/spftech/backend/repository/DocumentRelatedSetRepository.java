package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.entity.Document.DocumentRelatedSetId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRelatedSetRepository extends JpaRepository<DocumentRelatedSet, DocumentRelatedSetId> {

}
