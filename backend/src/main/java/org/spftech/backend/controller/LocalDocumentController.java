package org.spftech.backend.controller;

import java.util.List;
import java.util.Map;

import org.spftech.backend.dto.LocalDocumentDto;
import org.spftech.backend.entity.Document.LocalDocument;
import org.spftech.backend.service.LocalDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/local-documents")
public class LocalDocumentController {

    @Autowired
    private LocalDocumentService service;

    @GetMapping
    public ResponseEntity<Map<String, List<LocalDocumentDto>>> getAllLocalDocuments() {
        return new ResponseEntity<>(Map.of("data", service.getAllLocalDocuments()), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<LocalDocumentDto> createLocalDocument(
        @RequestBody LocalDocumentDto documentDto
    ) {
        LocalDocument saved_document = service.saveLocalDocument(service.toEntity(documentDto));

        return ResponseEntity.ok(service.toDto(saved_document));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalDocumentDto> getLocalDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getLocalDocumentById(id));
    }

}
