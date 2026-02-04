package org.spftech.backend.seeders;

import java.util.Arrays;
import java.util.List;

import org.spftech.backend.entity.Dossier;
import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.entity.Document.DocumentRelatedSetIdComposite;
import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.entity.Document.DocumentSet;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.entity.Document.LocalDocument;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.repository.DocumentRelatedSetRepository;
import org.spftech.backend.repository.DocumentRoleRepository;
import org.spftech.backend.repository.DocumentSetRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.DossierRepository;
import org.spftech.backend.repository.LocalDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class DocumentDBSeeder implements CommandLineRunner {
    
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private DocumentStateRepository documentStateRepository;
    @Autowired
    private DocumentRoleRepository documentRoleRepository;
    @Autowired
    private LocalDocumentRepository localDocumentRepository;
    @Autowired
    private DocumentReferenceRepository documentReferenceRepository;
    @Autowired
    private DocumentSetRepository documentSetRepository;
    @Autowired
    private DossierRepository dossierRepository;
    @Autowired
    private DocumentRelatedSetRepository documentRelatedSetRepository;

    @Override
    public void run(String... args) throws Exception {
            
        if (documentTypeRepository.count() == 0) {
            
            List<DocumentType> types = Arrays.asList(
                new DocumentType("ANNEXE 32", "Administratief", "Administrative", "Administratif", "Administrativ", "CDIS_ADMIN"),
                new DocumentType("ANNEXE 49", "Juridisch", "Legal", "Juridique", "Rechtlich", "CDIS_LEGAL")
            );

            documentTypeRepository.saveAll(types);
        } else {
            System.out.println("ℹ Types déjà existants");
        }

        if (documentStateRepository.count() == 0) {
            
            List<DocumentState> states = Arrays.asList(
                new DocumentState("DRAFT", "Concept", "Draft", "Brouillon", "Entwurf"),
                new DocumentState("FINAL", "Definitief", "Final", "Final", "Endgültig")
            );
            documentStateRepository.saveAll(states);
            
        } else {
            System.out.println("states déjà existants");
        }


        if (documentRoleRepository.count() == 0) {
            List<DocumentRole> roles = Arrays.asList(
                new DocumentRole("AUTHOR", "Auteur", "Author", "Auteur", "Autor", "CDIS_ROLE"),
                new DocumentRole("REVIEWER", "Revisor", "Reviewer", "Réviseur", "Prüfer", "CDIS_ROLE")
            );
            documentRoleRepository.saveAll(roles);
        } else {
            System.out.println("roles déjà existants");
        }

        if (localDocumentRepository.count() == 0) {
           DocumentType docType = documentTypeRepository.findById("ANNEXE 32")
               .orElseThrow(() -> new RuntimeException("Document type ANNEXE 32 not found"));
           DocumentState docState = documentStateRepository.findById("DRAFT")
               .orElseThrow(() -> new RuntimeException("Document state DRAFT not found"));

           LocalDocument localDoc = new LocalDocument();
           localDoc.setLabel("Sample local document");
           localDoc.setType(docType);
           localDoc.setPublished(new java.sql.Date(System.currentTimeMillis()));
           localDoc.setState(docState);
           localDoc.setArchived_as_docRef((short)0);

           LocalDocument localDoc1 = new LocalDocument();
           localDoc1.setLabel("ID document");
           localDoc1.setType(docType);
           localDoc1.setPublished(new java.sql.Date(System.currentTimeMillis()));
           localDoc1.setState(docState);
           localDoc1.setArchived_as_docRef((short)1);

           DocumentReference reference = new DocumentReference();
           reference.setNotes("Sample document reference");
           reference.setDocument_type(docType);
           reference.setId_doc_cdis("CDIS_001");
           reference.setId_coc_uri("URI_001");
           reference.setIs_doc_local((short)1);

           // parent <-> child
           reference.setLocal_Document(localDoc);
           localDoc.setDocumentReference(reference);

           localDocumentRepository.save(localDoc);
        //    localDocumentRepository.save(localDoc1);

           documentReferenceRepository.save(reference);

           System.out.println(" ----> DocumentDBSeeder: local document + reference créés.");
        }

        // Document SET avec le dossier de SUC 001
        if (documentSetRepository.count() == 0) {
            Dossier dossier = null;
            try {
                dossier = dossierRepository.findAll().get(0);
            } catch (Exception e) {
                System.out.println("\n\n\n\n"+ e.toString() + "\n\n\n\n");
            }

            
            DocumentSet docSet1 = new DocumentSet();
            docSet1.setLabel("Document Set 1");
            docSet1.setDossier(dossier);

            DocumentSet docSet2 = new DocumentSet();
            docSet2.setLabel("Document Set 2");
            docSet2.setDossier(dossier);

            documentSetRepository.save(docSet1);
            documentSetRepository.save(docSet2);

        }

        if (documentRelatedSetRepository.count() == 0) {

            DocumentReference documentReference = documentReferenceRepository.findAll()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No document reference found"));

            DocumentSet documentSet = documentSetRepository.findAll()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No document set found"));

            DocumentRole documentRole = documentRoleRepository.findById("AUTHOR")
                .orElseThrow(() -> new RuntimeException("Document role AUTHOR not found"));

            DocumentRelatedSetIdComposite id = new DocumentRelatedSetIdComposite();
            //     documentReference.getRef(),
            //     documentSet.getRef()
            // );

            DocumentRelatedSet documentRelatedSet = new DocumentRelatedSet();
            documentRelatedSet.setId(id);
            documentRelatedSet.setRank(1000);
            documentRelatedSet.setRole(documentRole);
            documentRelatedSet.setDocumentSet(documentSet);
            documentRelatedSet.setDocument(documentReference);

            try {
                documentRelatedSetRepository.save(documentRelatedSet);
                System.out.println("\n\n INSERTED \n\n ");
            } catch (Exception e) {
                System.out.println("\n\n NOT INSERTED: " + e.toString());
                throw e;
            }
        }
    }
}