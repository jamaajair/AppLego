package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.entity.Document.DocumentRelatedSetIdComposite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRelatedSetRepository extends JpaRepository<DocumentRelatedSet, DocumentRelatedSetIdComposite> {

}
