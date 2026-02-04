package org.spftech.backend.controller;

import org.spftech.backend.dto.DocumentUploadDto;
import org.spftech.backend.entity.Document.DocumentContent;
import org.spftech.backend.entity.Document.DocumentContentIdComposite;
import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.entity.Document.LocalDocument;
import org.spftech.backend.repository.DocumentContentRepository;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.LocalDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Clob;
import java.sql.Date;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.sql.rowset.serial.SerialClob;

@RestController
@RequestMapping("/api/documents")
public class DocumentContentController {

    @Autowired
    private LocalDocumentRepository localDocumentRepository;

    @Autowired
    private DocumentReferenceRepository documentReferenceRepository;

    @Autowired
    private DocumentContentRepository documentContentRepository;

    @Autowired
    private DocumentTypeRepository typeRepository;

    @Autowired
    private DocumentStateRepository stateRepository;

    private static final Set<String> ALLOWED_TYPES = Set.of(
    "application/pdf",
    "image/jpeg",
    "image/png"
    );



    @PostMapping("/content")
    public ResponseEntity<Map<String, Long>> uploadNewDocument(@RequestBody DocumentUploadDto dto){
        
        byte[] decoded = Base64.getDecoder().decode(dto.fileBase64());

        if (decoded.length > 10 * 1024 * 1024) {
            throw new RuntimeException("File too large (max 10MB)"); // alexandre : faut voir exception en dev web 
        }

        DocumentType type = typeRepository.findById(dto.type())
            .orElseThrow(() -> new RuntimeException("Invalid type"));

        if (!ALLOWED_TYPES.contains(dto.contentType())) {
            throw new RuntimeException("File type not allowed: " + dto.contentType());
        }

        DocumentState state = stateRepository.findById(dto.State())
                .orElseThrow(() -> new RuntimeException("Invalid state"));

        LocalDocument locDoc = new LocalDocument();
        locDoc.setLabel(dto.Label());
        locDoc.setType(type);
        locDoc.setPublished(dto.Published());
        locDoc.setState(state);
        locDoc.setArchived_as_docRef((short)1000);

        locDoc = localDocumentRepository.save(locDoc);
        DocumentReference docRef = new DocumentReference();
        // docRef.setRef(locDoc.getRef());                
        docRef.setLocal_Document(locDoc);              
        docRef.setDocument_type(type);                 
        docRef.setNotes(dto.Notes());
        docRef.setIs_doc_local((short) 1);
        docRef.setId_doc_cdis(null);                  
        docRef.setId_coc_uri(null);
        
        docRef = documentReferenceRepository.save(docRef);

        Clob clob;
        try {
            clob = new SerialClob(dto.fileBase64().toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer le CLOB : " + e.getMessage(), e);
        }

        Integer maxPart = documentContentRepository.findMaxPartNumber(locDoc.getRef());
        int nextPart = (maxPart == null ? 1 : maxPart + 1);
        DocumentContentIdComposite id =
            new DocumentContentIdComposite(locDoc.getRef(), nextPart);

        DocumentContent content = new DocumentContent();
        content.setId(id);
        content.setDocument(locDoc);
        content.setIana_content_type(dto.contentType());
        content.setContent(clob);
        
        documentContentRepository.save(content);

        return ResponseEntity.ok(Map.of("documentRef", locDoc.getRef()));
    }
}
