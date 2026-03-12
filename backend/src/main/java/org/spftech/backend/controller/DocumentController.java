package org.spftech.backend.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spftech.backend.dto.Document.AddDocumentToDossierDto;
import org.spftech.backend.dto.Document.LinkDocumentToDossierDto;
import org.spftech.backend.dto.CodeDto;
import org.spftech.backend.entity.Dossier;
import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.entity.Document.DocumentSet;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.repository.DocumentRoleRepository;
import org.spftech.backend.repository.DocumentSetRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.DossierRepository;
import org.spftech.backend.service.CodeMapper;
import org.spftech.backend.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class DocumentController{
    private final DocumentService documentService;
    private final DossierRepository dossierRepository;
    private final DocumentSetRepository documentSetRepository;
    private final DocumentRoleRepository documentRoleRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentStateRepository documentStateRepository;
    private final DocumentReferenceRepository documentReferenceRepository;

    @PostMapping("user/document/{document_id}/dossier/{dossier_id}/link")
    public ResponseEntity<Void> linkExistingDocumentToDossier(
        @PathVariable("dossier_id") String dossierId,
        @PathVariable("document_id") Long documentId, 
        @RequestBody LinkDocumentToDossierDto linkDocumentToDossierDto
    ) throws Exception {

        Dossier dossier = Utils.requireById(dossierRepository, dossierId, "Dossier");
        DocumentReference docRef = Utils.requireById(documentReferenceRepository, documentId, "DocumentReference");
        DocumentRole role = Utils.requireById(documentRoleRepository, linkDocumentToDossierDto.documentRoleCode(), "DocumentRole");
        documentService.linkDocumentToDossier(docRef, dossier, role);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("reference/document-types")
    public ResponseEntity<List<CodeDto>> getAllDocumentTypes() throws Exception {
        List<DocumentType> types = documentTypeRepository.findAll();
        if(types.isEmpty()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document types not found");

        return ResponseEntity.ok(CodeMapper.toCodeDtoList(types));
    }

    @GetMapping("reference/document-roles")
    public ResponseEntity<List<CodeDto>>  getAllDocumentRoles() throws Exception {
        List<DocumentRole> roles = documentRoleRepository.findAll();
        if(roles.isEmpty()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document roles not found");
        return ResponseEntity.ok(CodeMapper.toCodeDtoList(roles));
    }
    
    @GetMapping("reference/document-states")
    public ResponseEntity<List<CodeDto>> getAllDocumentStates() throws Exception {
        List<DocumentState> states = documentStateRepository.findAll();
        if(states.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Docment states not found");
        return ResponseEntity.ok(CodeMapper.toCodeDtoList(states));
    }
}