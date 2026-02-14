package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentContent;
import org.spftech.backend.entity.Document.DocumentContentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentContentRepository extends JpaRepository<DocumentContent, DocumentContentId> {
}