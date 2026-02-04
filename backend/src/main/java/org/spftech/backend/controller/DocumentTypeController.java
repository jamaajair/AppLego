package org.spftech.backend.controller;

import java.util.List;

import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/document-types")
public class DocumentTypeController {
    @Autowired
    private DocumentTypeService service;

    @PostMapping
    public DocumentType createDocumentType(@RequestBody DocumentType documentType) {
        return service.createDocumentType(documentType);
    }

    @GetMapping
    public List<DocumentType> getAllDocumentTypes() {
        return service.getAllDocumentTypes();
    }
}