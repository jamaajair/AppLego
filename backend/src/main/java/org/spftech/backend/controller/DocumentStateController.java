package org.spftech.backend.controller;

import java.util.List;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.service.DocumentStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/document-states")
public class DocumentStateController {
    @Autowired
    private DocumentStateService service;

    @PostMapping
    public DocumentState createDocumentState(@RequestBody DocumentState documentState) {
        return service.createDocumentState(documentState);
    }
    
    @GetMapping
    public List<DocumentState> getAllDocumentStates() {
        return service.getAllDocumentStates();
    }
    
}

