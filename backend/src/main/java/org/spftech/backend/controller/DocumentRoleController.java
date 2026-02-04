package org.spftech.backend.controller;

import java.util.List;

import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.service.DocumentRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/document-roles")
public class DocumentRoleController {

    @Autowired
    private DocumentRoleService service;

    @PostMapping
    public DocumentRole createDocumentRole(@RequestBody DocumentRole documentRole) {
        return service.createDocumentType(documentRole);
    }
    
    @GetMapping
    public List<DocumentRole> getAllDocumentRoles() {
        return  service.getAllDocumentRoles();
    }
    
    
}
