package org.spftech.backend.service;

import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.LocalDocument;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.entity.Document.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentReferenceService {

    @Autowired
    private DocumentReferenceRepository repository;

    // CRUD
    public DocumentReference createDocumentReference(DocumentReference documentReference) {
        return repository.save(documentReference);
    }

    public Optional<DocumentReference> getDocumentReferenceById(Long id) {
        return repository.findById(id);
    }

    public List<DocumentReference> getAllDocumentReferences() {
        return repository.findAll();
    }

    public void deleteDocumentReference(Long id) {
        repository.deleteById(id);
    }

    // public Optional<DocumentReference> findByLocalDocument(LocalDocument localDocument) {
    //     return repository.findByLocalDocument(localDocument);
    // }

    // public List<DocumentReference> findByDocumentType(DocumentType documentType) {
    //     return repository.findByDocumentType(documentType);
    // }

    // public DocumentReference updateDocumentReference(DocumentReference documentReference) {
    //     if (documentReference.getRef() == null) {
    //         throw new IllegalArgumentException("Document Reference must have an ID");
    //     }
    //     return repository.save(documentReference);
    // }
}