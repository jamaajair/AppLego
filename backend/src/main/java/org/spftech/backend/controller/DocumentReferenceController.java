package org.spftech.backend.controller;

import java.util.List;

import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.service.DocumentReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/documents-reference")
public class DocumentReferenceController {
    
    @Autowired
    private DocumentReferenceService service;

    @GetMapping
    public String getAllDocumentReferences(){
        if (service.getAllDocumentReferences().isEmpty()) {
            return "Empty";
        }
        return "service.getAllDocumentReferences().size()";
    }
}