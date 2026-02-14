package org.spftech.backend.seeders;

import java.util.Arrays;
import java.util.List;

import org.spftech.backend.dto.DocumentUploadDto;
import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.entity.Dossier;
import org.spftech.backend.repository.DocumentRoleRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.DossierRepository;
import org.spftech.backend.repository.LocalDocumentRepository;
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
    private final DossierRepository dossierRepository;

    private final DocumentService documentService;

    @Override
    public void run(String... args) throws Exception {
            
        if (documentTypeRepository.count() == 0) {  
            List<DocumentType> types = Arrays.asList(
                new DocumentType("ANNEXE 32", "Administratief", "Administrative", "Administratif", "Administrativ", "CDIS_ADMIN"),
                new DocumentType("ANNEXE 49", "Juridisch", "Legal", "Juridique", "Rechtlich", "CDIS_LEGAL")
            );
            documentTypeRepository.saveAll(types);
        }

        if (documentStateRepository.count() == 0) {
            List<DocumentState> states = Arrays.asList(
                new DocumentState("DRAFT", "Concept", "Draft", "Brouillon", "Entwurf"),
                new DocumentState("FINAL", "Definitief", "Final", "Final", "Endgültig")
            );
            documentStateRepository.saveAll(states);
        }

        if (documentRoleRepository.count() == 0) {
            List<DocumentRole> roles = Arrays.asList(
                new DocumentRole("AUTHOR", "Auteur", "Author", "Auteur", "Autor", "CDIS_ROLE"),
                new DocumentRole("REVIEWER", "Revisor", "Reviewer", "Réviseur", "Prüfer", "CDIS_ROLE")
            );
            documentRoleRepository.saveAll(roles);
        } 

        if (localDocumentRepository.count() == 0) {
            Dossier dossier = dossierRepository.findAll().get(0);
            DocumentType documentType = documentTypeRepository.findById("ANNEXE 32")
                .orElseThrow(() -> new RuntimeException("Document type ANNEXE 32 not found"));
            DocumentState documentState = documentStateRepository.findById("DRAFT")
                .orElseThrow(() -> new RuntimeException("Document state DRAFT not found"));
            

            documentService.addDocument(
                new DocumentUploadDto(
                    dossier.getRef(),
                    documentType.getCode(),
                    documentState.getCode(),
                    "Sample local document",
                    "URI",
                    null,
                    "https://picsum.photos/id/237/200/300"
                ),
                null
            );
        }
    }
}