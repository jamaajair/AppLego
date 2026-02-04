package org.spftech.backend.service;

import java.util.List;

import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentTypeService {
    @Autowired
    private DocumentTypeRepository repository;

    public DocumentType createDocumentType(DocumentType documentType) {
        return repository.save(documentType);
    }

    public List<DocumentType> getAllDocumentTypes() {
        return repository.findAll();
    }
}