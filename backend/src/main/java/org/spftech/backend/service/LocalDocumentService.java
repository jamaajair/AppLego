package org.spftech.backend.service;

import java.util.List;

import org.spftech.backend.dto.DocumentReferenceDto;
import org.spftech.backend.dto.LocalDocumentDto;
import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.entity.Document.LocalDocument;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.LocalDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class LocalDocumentService {
    
    @Autowired
    private LocalDocumentRepository repository;

    @Autowired
    private DocumentTypeRepository type_repository;

    @Autowired
    private DocumentStateRepository state_respository;

    @Autowired
    private DocumentReferenceRepository reference_repository;

    public LocalDocument createLocalDocument(LocalDocument localDocument){
        return repository.save(localDocument);
    }

    public List<LocalDocumentDto> getAllLocalDocuments() {
        return repository.findAll()
            .stream()
            .map(this::toDto)
            .toList();
    }

    public LocalDocumentDto toDto(LocalDocument doc) {
        DocumentReferenceDto refDto = null;

        if (doc.getDocumentReference() != null) {
            refDto = new DocumentReferenceDto(
                doc.getDocumentReference().getNotes(),
                doc.getDocumentReference().getId_doc_cdis(),
                doc.getDocumentReference().getId_coc_uri(),
                doc.getDocumentReference().getIs_doc_local()
            );
        }

        return new LocalDocumentDto(
            doc.getRef(),
            doc.getLabel(),
            doc.getType().getCode(),
            doc.getState().getCode(),
            doc.getPublished(),
            doc.getArchived_as_docRef(),
            refDto
        );
    }

    public LocalDocument toEntity(LocalDocumentDto dto){

        LocalDocument localDocument = new LocalDocument();
        localDocument.setLabel(dto.label());
        localDocument.setArchived_as_docRef(dto.archivedAsDocRef());
        localDocument.setPublished(dto.publishedDate());

        DocumentType type = type_repository.findById(dto.typeCode())
                            .orElseThrow(() -> new RuntimeException(" Type not found " + dto.typeCode()));
        localDocument.setType(type);

        DocumentState state = state_respository.findById(dto.stateCode())
                            .orElseThrow(() -> new RuntimeException(" State not found " + dto.stateCode()));
        localDocument.setState(state);

        DocumentReference ref = new DocumentReference();
        ref.setLocal_Document(localDocument);
        localDocument.setDocumentReference(ref);

        ref.setNotes(dto.reference().notes());
        ref.setId_doc_cdis(dto.reference().id_doc_cdis());
        ref.setId_coc_uri(dto.reference().id_doc_uri());
        ref.setIs_doc_local(dto.reference().is_doc_local());

        return localDocument;
    }

    public LocalDocument saveLocalDocument(LocalDocument localDocument){
        LocalDocument saved = repository.save(localDocument);

        DocumentReference ref = saved.getDocumentReference();

        ref.setRef(saved.getRef());

        reference_repository.save(ref);
        return saved;
    }

    public LocalDocumentDto getLocalDocumentById(Long id) {
    LocalDocument doc = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("LocalDocument not found with id: " + id));

    return toDto(doc);
}

}
