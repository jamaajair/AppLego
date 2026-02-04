package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentContent;
import org.spftech.backend.entity.Document.DocumentContentIdComposite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentContentRepository extends JpaRepository<DocumentContent, DocumentContentIdComposite> {
    @Query("SELECT COALESCE(MAX(dc.id.part_number), 0) FROM DocumentContent dc WHERE dc.id.documentN = :docRef")
    Integer findMaxPartNumber(@Param("docRef") Long docRef);

}