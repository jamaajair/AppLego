package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.LocalDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalDocumentRepository extends JpaRepository<LocalDocument, Long> {
    // @Query("SELECT DISTINCT ld FROM LocalDocument ld " +
    //    "JOIN ld.documentReference dr " +
    //    "JOIN DocumentRelatedSet drs ON dr.ref = drs.document.ref " +
    //    "JOIN drs.documentSet ds " +
    //    "WHERE ds.dossier.ref = :dossierRef")
    // List<LocalDocument> findDocumentsByDossierRef(@Param("dossierRef") String dossierRef);
}

            