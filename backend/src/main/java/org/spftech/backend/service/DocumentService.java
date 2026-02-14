package org.spftech.backend.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.spftech.backend.dto.DocumentUploadDto;
import org.spftech.backend.entity.Document.DocumentContent;
import org.spftech.backend.entity.Document.DocumentContentId;
import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.entity.Document.DocumentRelatedSetId;
import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.entity.Document.DocumentSet;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.entity.Document.LocalDocument;
import org.spftech.backend.entity.Dossier;
import org.spftech.backend.repository.DocumentContentRepository;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.repository.DocumentRelatedSetRepository;
import org.spftech.backend.repository.DocumentRoleRepository;
import org.spftech.backend.repository.DocumentSetRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.DossierRepository;
import org.spftech.backend.repository.LocalDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DocumentService {
    private final DossierRepository dossierRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentStateRepository documentStateRepository;
    private final DocumentSetRepository documentSetRepository;
    private final LocalDocumentRepository localDocumentRepository;
    private final DocumentRelatedSetRepository documentRelatedSetRepository;
    private final DocumentContentRepository documentContentRepository;
    private final DocumentReferenceRepository documentReferenceRepository;
    private final DocumentRoleRepository documentRoleRepository;

    @Transactional
    public void addDocument(DocumentUploadDto documentUploadDto, MultipartFile file) {
        Dossier dossier = dossierRepository.findById(documentUploadDto.dossierRef())
            .orElseThrow(() -> new RuntimeException("Folder not found with ref: " + documentUploadDto.dossierRef()));
        DocumentType documentType = documentTypeRepository.findById(documentUploadDto.documentTypeCode())
            .orElseThrow(() -> new RuntimeException("Document type not found with : " + documentUploadDto.documentTypeCode()));
        DocumentState documentState = documentStateRepository.findById(documentUploadDto.documentStateCode())
            .orElseThrow(() -> new RuntimeException("Document state not found with : " + documentUploadDto.documentStateCode()));
        DocumentSet documentSet = documentSetRepository.findByBoundToDossier(dossier)
            .orElseGet(() -> {
                DocumentSet ds = new DocumentSet();
                ds.setBoundToDossier(dossier);
                ds.setLabel("Document set for dossier " + dossier.getRef());
                return documentSetRepository.save(ds);
            });
   
        // Still create local document even if no file is uploaded (as seen in Software Document Set)
        LocalDocument localDocument = new LocalDocument();
        localDocument.setLabel(documentUploadDto.localDocumentLabel());
        localDocument.setType(documentType);
        localDocument.setPublished(new Date());
        localDocument.setState(documentState);
        localDocument.setArchivedAsDocRef((short) 0);
        localDocument = localDocumentRepository.save(localDocument);

        DocumentReference documentReference = new DocumentReference();
        documentReference.setLocalDocument(localDocument); 
        documentReference.setType(documentType);
        documentReference.setNotes("");
        documentReference.setIsDocLocal((documentUploadDto.CDISorURIorLOCAL().equals("LOCAL")) ? (short) 1 : (short) 0);
        if(documentUploadDto.CDISorURIorLOCAL().equals("CDIS")) documentReference.setIdDocCdis(documentUploadDto.CDIS());
        if(documentUploadDto.CDISorURIorLOCAL().equals("URI")) documentReference.setIdDocUri(documentUploadDto.URI());
        documentReferenceRepository.save(documentReference);

        DocumentRelatedSet documentRelatedSet = new DocumentRelatedSet();
        documentRelatedSet.setId(new DocumentRelatedSetId(documentReference.getRef(), documentSet.getRef()));
        documentRelatedSet.setDocument(documentReference);
        documentRelatedSet.setInSet(documentSet); 
        // TODO: add RANK_IN_SET (compute the numbers of documents already in the set and add 1)
        // Ignore ROLE_IN_SET for now
        documentRelatedSetRepository.save(documentRelatedSet);
        
        final Set<String> ALLOWED = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png"
        );
        if(documentReference.getIsDocLocal() == 1) {
            try{
                if (file == null || file.isEmpty()) {
                    throw new RuntimeException("LOCAL requires a file.");
                }

                String ct = file.getContentType();
                if (ct == null || !ALLOWED.contains(ct.toLowerCase())) {
                    throw new RuntimeException("Only PDF/JPEG/PNG allowed.");
                }

                String base64 = Base64.getEncoder().encodeToString(file.getBytes());

                DocumentContent dc = new DocumentContent();
                dc.setId(new DocumentContentId(localDocument.getRef(), 0));
                dc.setIanaContentType(ct.toLowerCase());
                dc.setContent(base64);
                dc.setDocRef(localDocument);
                documentContentRepository.save(dc);

            }catch(IOException e){
                throw new RuntimeException("Error Encoding content: " + e.getMessage(), e);
            }
        }
    }

    // public List<LocalDocument> getDocumentsForDossier(String dossierRef) {
    //     return localDocumentRepository.findByDocumentReferenceInSetBoundToDossierRef(dossierRef);
    // }

    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }

    public List<DocumentRole> getAllDocumentRoles() {
        return documentRoleRepository.findAll();
    }

    public List<DocumentState> getAllDocumentStates() {
        return documentStateRepository.findAll();
    }
    
}

// Old LocalDocumentService code for reference, not to be included in the final version
/*  public List<LocalDocumentDto> getAllLocalDocuments() {
    //     return repository.findAll()
    //         .stream()
    //         .map(this::toDto)
    //         .toList();
    // }

    // public LocalDocumentDto toDto(LocalDocument doc) {
    //     DocumentReferenceDto refDto = null;

    //     if (doc.getDocumentReference() != null) {
    //         refDto = new DocumentReferenceDto(
    //             doc.getDocumentReference().getNotes(),
    //             doc.getDocumentReference().getId_doc_cdis(),
    //             doc.getDocumentReference().getId_coc_uri(),
    //             doc.getDocumentReference().getIs_doc_local()
    //         );
    //     }

    //     return new LocalDocumentDto(
    //         doc.getRef(),
    //         doc.getLabel(),
    //         doc.getType().getCode(),
    //         doc.getState().getCode(),
    //         doc.getPublished(),
    //         doc.getArchivedAsDocRef(),
    //         refDto
    //     );
    // }

    // public LocalDocument toEntity(LocalDocumentDto dto){

    //     LocalDocument localDocument = new LocalDocument();
    //     localDocument.setLabel(dto.label());
    //     localDocument.setArchivedAsDocRef(dto.archivedAsDocRef());
    //     localDocument.setPublished(dto.publishedDate());

    //     DocumentType type = type_repository.findById(dto.typeCode())
    //                         .orElseThrow(() -> new RuntimeException(" Type not found " + dto.typeCode()));
    //     localDocument.setType(type);

    //     DocumentState state = state_respository.findById(dto.stateCode())
    //                         .orElseThrow(() -> new RuntimeException(" State not found " + dto.stateCode()));
    //     localDocument.setState(state);

    //     DocumentReference ref = new DocumentReference();
    //     ref.setLocal_Document(localDocument);
    //     localDocument.setDocumentReference(ref);

    //     ref.setNotes(dto.reference().notes());
    //     ref.setId_doc_cdis(dto.reference().id_doc_cdis());
    //     ref.setId_coc_uri(dto.reference().id_doc_uri());
    //     ref.setIs_doc_local(dto.reference().is_doc_local());

    //     return localDocument;
    // }

    // public LocalDocument saveLocalDocument(LocalDocument localDocument){
    //     LocalDocument saved = repository.save(localDocument);

    //     DocumentReference ref = saved.getDocumentReference();

    //     ref.setRef(saved.getRef());

    //     reference_repository.save(ref);
    //     return saved;
    // }

    // public LocalDocumentDto getLocalDocumentById(Long id) {
    //     LocalDocument doc = repository.findById(id)
    //         .orElseThrow(() -> new RuntimeException("LocalDocument not found with id: " + id));

    //     return toDto(doc);
    // }

    // public List<LocalDocumentDto> getDocumentsForDossier(String dossierRef) {
    //     // System.out.println("Recherche de documents pour le dossier : " + dossierRef);
        
    //     List<LocalDocument> documents = repository.findDocumentsByDossierRef(dossierRef);
        
    //     // System.out.println("Nombre de documents trouvés : " + documents.size());
        
    //     // documents.forEach(doc -> {
    //     //     System.out.println("Document trouvé - Ref: " + doc.getRef() + 
    //     //                     ", Label: " + doc.getLabel() + 
    //     //                     ", Type: " + doc.getType().getCode() + 
    //     //                     ", State: " + doc.getState().getCode());
    //     // });
        
    //     return documents.stream()
    //         .map(this::toDto)
    //         .collect(Collectors.toList());
    // }
*/