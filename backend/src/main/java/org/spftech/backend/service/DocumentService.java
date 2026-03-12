package org.spftech.backend.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.spftech.backend.entity.Document.DocumentContent;
import org.spftech.backend.entity.Document.DocumentContentId;
import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.entity.Document.DocumentRelatedSetId;
import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.entity.Document.DocumentSet;
import org.spftech.backend.entity.Document.DocumentSetBinding;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.entity.Document.LocalDocument;
import org.spftech.backend.repository.DocumentContentRepository;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.repository.DocumentRelatedSetRepository;
import org.spftech.backend.repository.DocumentRoleRepository;
import org.spftech.backend.repository.DocumentSetRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.DossierRepository;
import org.spftech.backend.repository.LocalDocumentRepository;
import org.spftech.backend.entity.Asset;
import org.spftech.backend.entity.Dossier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DocumentService {
    private final DocumentSetRepository documentSetRepository;
    private final LocalDocumentRepository localDocumentRepository;
    private final DocumentRelatedSetRepository documentRelatedSetRepository;
    private final DocumentContentRepository documentContentRepository;
    private final DocumentReferenceRepository documentReferenceRepository;

    @Transactional
    public LocalDocument addLocalDocument(
        DocumentType documentType, 
        DocumentState documentState, 
        String label, 
        MultipartFile file) throws IOException 
    {
        final int CHUNK_SIZE = 4 * 1024 * 1024; //4MB chunks, since the MariaDB max_allowed_packet is 16MB by default && MEDIUMBLOB max size is 16MB (safe room for headers and other data)

        LocalDocument localDocument = new LocalDocument();
        localDocument.setLabel(label);
        localDocument.setType(documentType);
        localDocument.setPublished(new Date());
        localDocument.setState(documentState);
        localDocument.setArchivedAsDocRef((short) 0);
        localDocument = localDocumentRepository.save(localDocument);

        int partNumber = 0;
        try (var in = file.getInputStream()) {

            byte[] buffer = new byte[CHUNK_SIZE];
            int read;
            while ((read = in.read(buffer)) != -1) {
                byte[] chunk = Arrays.copyOf(buffer, read);

                DocumentContent documentContent = new DocumentContent();
                documentContent.setId(new DocumentContentId(localDocument.getRef(), partNumber++));
                documentContent.setIanaContentType(file.getContentType());
                documentContent.setContent(chunk);
                documentContent.setDocRef(localDocument);

                documentContentRepository.save(documentContent);
            }
        }
        
        //Add related_author & related_signatory later
        return localDocument;
    }

    @Transactional
    public DocumentReference addDocument(
        DocumentType documentType, 
        DocumentState documentState, 
        String localDocumentLabel, 
        String documentReferenceNotes,
        String CDIS,
        String URI,
        MultipartFile file) throws IOException 
    {
        // if the document is local/file => isDocLocal = 1, else (CDIS or URI) => isDocLocal = 0
        DocumentReference documentReference = new DocumentReference();
        documentReference.setNotes(documentReferenceNotes);
        documentReference.setType(documentType);
        if(CDIS != null && !CDIS.isEmpty()) { 
            documentReference.setIdDocCdis(CDIS);
            documentReference.setIsDocLocal((short) 0);

        } else if(URI != null && !URI.isEmpty()) {
            documentReference.setIdDocUri(URI);
            documentReference.setIsDocLocal((short) 0);

        } else if(file != null && !file.isEmpty()) {
            LocalDocument localDocument = addLocalDocument(documentType, documentState, localDocumentLabel, file);
            documentReference.setLocalDocument(localDocument);
            documentReference.setIsDocLocal((short) 1);

        } else {
            throw new RuntimeException("Invalid document type: must be CDIS, URI, or LOCAL");
        }

        return documentReferenceRepository.save(documentReference);
    }

    @Transactional
    public DocumentSet createDocumentSet(
        Asset asset, 
        Dossier dossier, 
        DocumentSetBinding documentSetBinding, 
        String label) 
    {
        DocumentSet documentSet = new DocumentSet();
        documentSet.setLabel(label != null && !label.isEmpty() ? label : "");
        if(dossier != null) documentSet.setBoundToDossier(dossier);
        if(asset != null) documentSet.setBoundToAsset(asset);
        documentSet.setBinding(documentSetBinding);
        return documentSetRepository.save(documentSet);
    }

    @Transactional 
    public DocumentRelatedSet linkDocumentToDossier(
        DocumentReference documentReference, 
        Dossier dossier, 
        DocumentRole documentRole) 
    {
        // Dossier should already have a DocumentSet
        DocumentSet documentSet = documentSetRepository.findByBoundToDossier(dossier)
            .orElseThrow(() -> new RuntimeException("Document set not found for dossier with id: " + dossier.getRef()));
        Long totalDocumentLinkedToDossier = documentRelatedSetRepository.countByInSet(documentSet);

        DocumentRelatedSetId id = new DocumentRelatedSetId(documentReference.getRef(), documentSet.getRef());
        DocumentRelatedSet documentRelatedSet = new DocumentRelatedSet();
        documentRelatedSet.setId(id);
        documentRelatedSet.setDocument(documentReference);
        documentRelatedSet.setRankInSet(totalDocumentLinkedToDossier.intValue());
        documentRelatedSet.setInSet(documentSet);
        documentRelatedSet.setRoleInSet(documentRole);
        return documentRelatedSetRepository.save(documentRelatedSet);
    }
     
    @Transactional
    public void unlinkDocumentFromDossier(Dossier dossier, DocumentReference documentReference) {

        DocumentSet documentSet = documentSetRepository.findByBoundToDossier(dossier)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Document set not found for dossier: " + dossier.getRef()
                )
            );

        DocumentRelatedSet link = documentRelatedSetRepository
            .findByInSetAndDocument(documentSet, documentReference)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Document is not linked to this dossier"
                )
            );

        documentRelatedSetRepository.delete(link);
    }

    public boolean isValidURI(String URI) {
        try {
            return new URI(URI).isAbsolute();
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public boolean isValidCDIS(String CDIS) {
        try {
            UUID.fromString(CDIS);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isValidLocalDocument(MultipartFile file) {
        if(!List.of("image/png", "image/jpeg", "application/pdf").contains(file.getContentType())) 
            return false;

        return true;
    }
}