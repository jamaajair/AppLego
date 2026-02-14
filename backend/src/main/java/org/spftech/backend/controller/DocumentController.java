package org.spftech.backend.controller;

import java.util.List;
import java.util.Map;

import org.spftech.backend.dto.DocumentUploadDto;
import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/document/")
public class DocumentController{
    private final DocumentService documentService;

    @PostMapping("upload")
    public ResponseEntity<Map<String, Object>> uploadDocument(
        @RequestBody DocumentUploadDto documentUploadDto, 
        @RequestPart(value= "file", required= false) MultipartFile file) {
        
        try {
            documentService.addDocument(documentUploadDto, file);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Bad request: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("data", " Upss Document uploaded successfully"));
    }

    @GetMapping("types")
    public ResponseEntity<Map<String, List<DocumentType>>> getAllDocumentTypes() {
        return ResponseEntity.ok(Map.of("data", documentService.getAllDocumentTypes()));
    }

    @GetMapping("roles")
    public ResponseEntity<Map<String, List<DocumentRole>>> getAllDocumentRoles() {
        return ResponseEntity.ok(Map.of("data", documentService.getAllDocumentRoles()));
    }
    
    @GetMapping("states")
    public ResponseEntity<Map<String, List<DocumentState>>> getAllDocumentStates() {
        return ResponseEntity.ok(Map.of("data", documentService.getAllDocumentStates()));
    }
    
}
