package org.spftech.backend.seeders;

import java.util.Arrays;
import java.util.List;

import javax.swing.text.Document;

import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.entity.Document.DocumentSet;
import org.spftech.backend.entity.Document.DocumentSetBinding;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.repository.DocumentContentRepository;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.repository.DocumentRelatedSetRepository;
import org.spftech.backend.repository.DocumentRoleRepository;
import org.spftech.backend.repository.DocumentSetBindingRepository;
import org.spftech.backend.repository.DocumentSetRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.DossierRepository;
import org.spftech.backend.repository.LocalDocumentRepository;
import org.spftech.backend.entity.Dossier;
import org.spftech.backend.service.DocumentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(3)
public class DocumentDBSeeder implements CommandLineRunner {
    
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentStateRepository documentStateRepository;
    private final DocumentRoleRepository documentRoleRepository;
    private final LocalDocumentRepository localDocumentRepository;
    private final DocumentSetBindingRepository documentSetBindingRepository;
    private final DocumentSetRepository documentSetRepository;
    private final DocumentReferenceRepository documentReferenceRepository;
    private final DocumentContentRepository documentContentRepository;
    private final DocumentRelatedSetRepository documentRelatedSetRepository;

    private final DossierRepository dossierRepository;
    private final DocumentService documentService;

    @Override
    public void run(String... args) throws Exception {

        if (documentReferenceRepository.count() > 0) {
            System.out.println("ℹ Doscuments déjà existants, seeding ignoré.");
            return;
        }
       
        if (documentSetRepository.count() > 0) {
            System.out.println("Dossiers already existing, seeding ignored.");
            return;
        }

        //Only 2 for now
        List<DocumentType> types = Arrays.asList(
            new DocumentType("ANNEXE 32", "Administratief", "Administrative", "Administratif", "Administrativ", "CDIS_ADMIN"),
            new DocumentType("ANNEXE 49", "Juridisch", "Legal", "Juridique", "Rechtlich", "CDIS_LEGAL")
        );
        documentTypeRepository.saveAll(types);
    

        List<DocumentState> states = Arrays.asList(
            new DocumentState("INITIAL", "Initieel", "Initial", "Initial", "Initial"),
            new DocumentState("EDITED", "Bewerkt", "Edited", "Édité", "Bearbeitet"),
            new DocumentState("UNDER_REVIEW", "In beoordeling", "Under Review", "En cours d'évaluation", "Unter Bewertung"),
            new DocumentState("APPROVED", "Goedgekeurd", "Approved", "Approuvé", "Genehmigt"),
            new DocumentState("ARCHIVED", "Gearchiveerd", "Archived", "Archivé", "Archiviert"),
            new DocumentState("DROPPED", "Verwijderd", "Dropped", "Supprimé", "Entfernt")
        );
        documentStateRepository.saveAll(states);
        

        //Only 2 for now
        List<DocumentRole> roles = Arrays.asList(
            new DocumentRole("WORK_DOCUMENT", "Werkdocument", "Work Document", "Document de travail", "Arbeitsdokument", "CDIS_WORK"),
            new DocumentRole("CALCUL_NOTE", "Berekening", "Calculation Note", "Note de calcul", "Berechnungsnotiz", "CDIS_CALC")
        );
        documentRoleRepository.saveAll(roles);

        List<DocumentSetBinding> bindings = Arrays.asList(
            new DocumentSetBinding("D", "Dossier", "Dossier", "Dossier", "Dossier"),
            new DocumentSetBinding("T", "Transactie", "Transaction", "Transaction", "Transaktion"),
            new DocumentSetBinding("A", "Asset", "Asset", "Asset", "Asset"),
            new DocumentSetBinding("M", "Mandate", "Mandate", "Mandat", "Mandat")
        );
        documentSetBindingRepository.saveAll(bindings);


        List<Dossier> dossiers = dossierRepository.findAll();
        DocumentType documentType = documentTypeRepository.findById("ANNEXE 32")
            .orElseThrow(() -> new RuntimeException("Document type ANNEXE 32 not found"));
        DocumentState documentState = documentStateRepository.findById("INITIAL")
            .orElseThrow(() -> new RuntimeException("Document state INITIAL not found"));
        DocumentSetBinding documentSetBinding = documentSetBindingRepository.findById("D")
            .orElseThrow(() -> new RuntimeException("Document set binding D not found"));
        DocumentRole documentRole = documentRoleRepository.findById("WORK_DOCUMENT")
            .orElseThrow(() -> new RuntimeException("Document role WORK_DOCUMENT not found"));

        for(Dossier dossier : dossiers) {
            documentService.createDocumentSet(null, dossier, documentSetBinding, "Document Set for Dossier: " + dossier.getRef());
        }
        DocumentReference documentReference = documentService.addDocument(documentType, documentState, "Document for Dossier: " + dossiers.get(0).getRef(), "Notes for the document", null, "https://picsum.photos/200/300", null);
        documentService.linkDocumentToDossier(documentReference, dossiers.get(0), documentRole);
        
    }
}