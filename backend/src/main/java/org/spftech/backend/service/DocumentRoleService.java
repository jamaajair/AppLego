package org.spftech.backend.service;

import java.util.List;

import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.repository.DocumentRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentRoleService {
    @Autowired
    private DocumentRoleRepository repository;

    public DocumentRole createDocumentType(DocumentRole documentRole){
        return repository.save(documentRole);
    }

    public List<DocumentRole> getAllDocumentRoles(){
        return repository.findAll();
    }
}
