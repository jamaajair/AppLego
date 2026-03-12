package org.spftech.backend.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.spftech.backend.dto.Document.AddDocumentToDossierDto;
import org.spftech.backend.dto.Document.DocumentListItemDto;
import org.spftech.backend.dto.CodeDto;
import org.spftech.backend.dto.DossierDto;
import org.spftech.backend.dto.LinkKindDTO;
import org.spftech.backend.dto.LinkedDossierDTO;
import org.spftech.backend.dto.NewDossierDto;
import org.spftech.backend.dto.NewLinkedDossierDTO;
import org.spftech.backend.dto.PatchDossierDto;
import org.spftech.backend.entity.Code;
import org.spftech.backend.entity.Dossier;
import org.spftech.backend.entity.DossierState;
import org.spftech.backend.entity.DossierType;
import org.spftech.backend.entity.LinkKind;
import org.spftech.backend.entity.LinkedDossier;
import org.spftech.backend.entity.LinkedDossierId;
import org.spftech.backend.entity.Document.DocumentReference;
import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.entity.Document.DocumentRole;
import org.spftech.backend.entity.Document.DocumentSet;
import org.spftech.backend.entity.Document.DocumentSetBinding;
import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.entity.Document.DocumentType;
import org.spftech.backend.repository.CodeRepository;
import org.spftech.backend.repository.DocumentReferenceRepository;
import org.spftech.backend.repository.DocumentRelatedSetRepository;
import org.spftech.backend.repository.DocumentRoleRepository;
import org.spftech.backend.repository.DocumentSetBindingRepository;
import org.spftech.backend.repository.DocumentSetRepository;
import org.spftech.backend.repository.DocumentStateRepository;
import org.spftech.backend.repository.DocumentTypeRepository;
import org.spftech.backend.repository.DossierRepository;
import org.spftech.backend.repository.DossierStateRepository;
import org.spftech.backend.repository.DossierTypeRepository;
import org.spftech.backend.repository.LinkKindRepository;
import org.spftech.backend.repository.LinkedDossierRepository;
import org.spftech.backend.service.DocumentService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/")
@PreAuthorize("hasAnyRole('EMPLOYEES', 'ADMIN', 'SUPERVISORS')")
public class DossierController{
    private final DossierRepository dossierRepository;
    private final DossierTypeRepository dossierTypeRepository;
    private final DossierStateRepository dossierStateRepository;
    private final LinkKindRepository linkKindRepository;
    private final LinkedDossierRepository linkedDossierRepository;
    private final CodeRepository codeRepository;
    private final DocumentService documentService;
    private final DocumentSetBindingRepository documentSetBindingRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentStateRepository documentStateRepository;
    private final DocumentRoleRepository documentRoleRepository;
    private final DocumentSetRepository documentSetRepository;
    private final DocumentRelatedSetRepository documentRelatedSetRepository;
    private final DocumentReferenceRepository documentReferenceRepository;

    @PostMapping("dossier/new")
    public ResponseEntity<Map<String, Object>> createDossier(@RequestBody NewDossierDto dossierDto){
        Code stateCode = codeRepository.findByCodeValueAndUsageContext("N", "DOSSIER_STATE")
                .stream().findFirst().orElse(null);
        Code typeCode = codeRepository.findByCodeValueAndUsageContext(dossierDto.type(), "DOSSIER_TYPE")
                .stream().findFirst().orElse(null);

        if(typeCode == null || stateCode == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(dossierRepository.existsByRef(dossierDto.ref())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        DossierState state = new DossierState(stateCode);
        DossierType type = new DossierType(typeCode);

        dossierStateRepository.save(state);
        dossierTypeRepository.save(type);

        Dossier dossier = new Dossier();

        if(dossier.getStartNoLaterThan() != null && dossier.getExpectCompletion() != null) {
            if(dossier.getStartNoLaterThan().after(dossier.getExpectCompletion())) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "startNoLaterThan cannot be after expectCompletion"));
            }
        }

        dossier.setRef(dossierDto.ref());
        dossier.setLabel(dossierDto.label());
        dossier.setCreatedAt(new Date());
        dossier.setExpectCompletion(dossierDto.expectCompletion());
        dossier.setStartNoLaterThan(dossierDto.startNoLaterThan());
        dossier.setComments(dossierDto.comments());
        dossier.setType(type);
        dossier.setState(state);

        Dossier saved = dossierRepository.save(dossier);

        DocumentSetBinding documentSetBinding = documentSetBindingRepository.findById("D")
            .orElseThrow(() -> new RuntimeException("Document set binding not found with code: D"));
        documentService.createDocumentSet(null, dossier, documentSetBinding, "Document Set for Dossier " + dossier.getRef());


        if(saved == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Map.of("data", saved), HttpStatus.CREATED);
    }

    private Integer searchChildren(String sourceRef, String compRef){
        List<LinkedDossier> linkedDossierSourceAsParent = linkedDossierRepository.findByParentDossierRef(sourceRef);

        Integer foundRef = 0;
        for(LinkedDossier linkedDossier : linkedDossierSourceAsParent){
            String childRef = linkedDossier.getChildDossier().getRef();
            if(childRef.equals(compRef))
                return 1;
            foundRef += searchChildren(childRef, compRef);
        }

        return foundRef;
    }

    @PostMapping("dossier/link")
    public ResponseEntity<Map<String, LinkedDossierDTO>> createLinkedDossier(@RequestBody NewLinkedDossierDTO linkedDossierDto){
        Dossier parentDossier = dossierRepository.findById(linkedDossierDto.parentDossierRef()).orElse(null);
        Dossier childDossier = dossierRepository.findById(linkedDossierDto.childDossierRef()).orElse(null);
        Code linkKindCode = codeRepository.findByCodeValueAndUsageContext(linkedDossierDto.linkKind(), "LINK_KIND")
                .stream().findFirst().orElse(null);
        if(linkKindCode == null || childDossier == null || parentDossier == null || linkedDossierDto.sequenceNumber() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //check if child and parent aren't the same
        if(parentDossier.getRef().equals(childDossier.getRef())){
            System.out.println("Child " + childDossier.getRef() + " is the same as Parent " + parentDossier.getRef());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //check if the child isn't a descendant of parent already  
        if(searchChildren(parentDossier.getRef(), childDossier.getRef()) > 0){
            System.out.println("Child " + childDossier.getRef() + " already descendant of Parent " + parentDossier.getRef());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //check if the child isn't an ancestor of parent (avoid cycles) 
        if(searchChildren(childDossier.getRef(), parentDossier.getRef()) > 0){
            System.out.println("Child " + childDossier.getRef() + " is an ancestor of Parent " + parentDossier.getRef());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LinkKind linkKind = new LinkKind(linkKindCode);
        LinkKind linkKindSaved = linkKindRepository.save(linkKind);

        LinkedDossier linkedDossier = new LinkedDossier(
          parentDossier,
          childDossier,
          linkKindSaved,
          linkedDossierDto.sequenceNumber()
        );

        LinkedDossier saved = linkedDossierRepository.save(linkedDossier);
        if(saved == null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        LinkedDossierDTO linkedDossierDTO = Utils.toDTO(saved);

        return new ResponseEntity<>(Map.of("data", linkedDossierDTO), HttpStatus.OK);
    }

    @DeleteMapping("dossier/unlink")
    public ResponseEntity<String> unlinkDossiers(@RequestBody NewLinkedDossierDTO linkedDossierDTO){
        LinkedDossierId linkDossierId = new LinkedDossierId(linkedDossierDTO.parentDossierRef(), linkedDossierDTO.childDossierRef());
        System.out.println("LINK DOSSIER ID TO DELETE: " + linkDossierId);

        LinkedDossier linkedDossier = linkedDossierRepository.findById(linkDossierId).orElse(null);

        if(linkedDossier == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        linkedDossierRepository.deleteById(linkDossierId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("dossier/{id}/fork")
    public ResponseEntity<Map<String, LinkedDossierDTO>> createdForkedDossier(@PathVariable String id){
        Dossier dossierToFork = dossierRepository.findById(id).orElse(null);
        if(dossierToFork == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Dossier> existingForks = dossierRepository.findAll().stream().filter(dossier -> dossier.getRef().contains(dossierToFork.getRef() + "-Forked")).toList();
        String forkedDossierRef = dossierToFork.getRef() + "-Forked";
        if(!existingForks.isEmpty())
            forkedDossierRef = forkedDossierRef + (existingForks.size() + 1);


        Dossier dossierForked = new Dossier(
            forkedDossierRef,
            dossierToFork.getType(),
            dossierToFork.getLabel(),
            new Date(),
            "",
            dossierToFork.getState(),
            dossierToFork.getStartNoLaterThan(),
            dossierToFork.getExpectCompletion()
        );
        Dossier dossierSaved = dossierRepository.save(dossierForked);

        Code linkKindCode = codeRepository.findByCodeValueAndUsageContext("CLONE", "LINK_KIND").stream().findFirst().orElse(null);
        LinkKind linkKind = new LinkKind(linkKindCode);
        linkKind = linkKindRepository.save(linkKind);

        LinkedDossier linkedDossier = new LinkedDossier(
            dossierToFork,
            dossierSaved,
            linkKind,
            1
        );
        linkedDossierRepository.save(linkedDossier);
        
        LinkedDossierDTO linkedDossierDTO = Utils.toDTO(linkedDossier);
        return new ResponseEntity<>(Map.of("data", linkedDossierDTO), HttpStatus.OK);
    }

    @GetMapping("linked_dossier/{parent_id}")
    public ResponseEntity<Map<String, List<LinkedDossierDTO>>> getMethodName(@PathVariable String parent_id) {
        List<LinkedDossier> linkedDossiers = linkedDossierRepository.findByParentDossierRef(parent_id);

        List<LinkedDossierDTO> linkedDossierDTOs = linkedDossiers.stream().map(
            linkedDossier -> Utils.toDTO(linkedDossier)
        ).toList();

        return new ResponseEntity<>(Map.of("data", linkedDossierDTOs), HttpStatus.OK);
    }
    

    @GetMapping("link_kinds")
    public ResponseEntity<Map<String, List<LinkKindDTO>>> getAllLinkKinds(){
        List<LinkKind> linkKinds = linkKindRepository.findAllWithCode();
        
        List<LinkKindDTO> linkKindsDTO = linkKinds.stream().map(
            linkKind -> new LinkKindDTO(
                linkKind.getId(),
                new CodeDto(
                    linkKind.getCode().getCodeValue(),
                    linkKind.getCode().getLabelNl(),
                    linkKind.getCode().getLabelEn(),
                    linkKind.getCode().getLabelFr(),
                    linkKind.getCode().getLabelDe()
                )
            )
        ).toList();

        return new ResponseEntity<>(Map.of("data", linkKindsDTO), HttpStatus.OK);
    }

    @GetMapping ("code/{usage_context}")
    public ResponseEntity<Map<String, List<CodeDto>>> getCodeForUsage(@PathVariable String usage_context){
        List<CodeDto> codesDTO = codeRepository.findByUsageContext(usage_context).stream().map(code -> Utils.toDTO(code)).toList();
        return new ResponseEntity<>(Map.of("data", codesDTO), HttpStatus.OK);
    }


    @GetMapping("my_dossiers")
    public ResponseEntity<Map<String, List<DossierDto>>> getAllDossiers() {
        try {
            System.out.println("HELLO WORLD - je suis dans le controller");
            List<DossierDto> dossiers = dossierRepository.findAll().stream().map(
                    dossier -> new DossierDto(
                            dossier.getRef(),
                            dossier.getLabel(),
                            dossier.getComments(),
                            dossier.getCreatedAt(),
                            dossier.getStartNoLaterThan(),
                            dossier.getExpectCompletion(),
                            new CodeDto(
                                    dossier.getState().getCode().getCodeValue(),
                                    dossier.getState().getCode().getLabelNl(),
                                    dossier.getState().getCode().getLabelEn(),
                                    dossier.getState().getCode().getLabelFr(),
                                    dossier.getState().getCode().getLabelDe()
                            ),
                            new CodeDto(
                                    dossier.getType().getCode().getCodeValue(),
                                    dossier.getType().getCode().getLabelNl(),
                                    dossier.getType().getCode().getLabelEn(),
                                    dossier.getType().getCode().getLabelFr(),
                                    dossier.getType().getCode().getLabelDe()
                            )
                    )
            ).toList();
            return new ResponseEntity<>(Map.of("data", dossiers), HttpStatus.OK);
        } catch (DataAccessException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("dossier/{id}")
    public ResponseEntity<Map<String, DossierDto>> getDossierById(@PathVariable String id){
        Dossier dossier = dossierRepository.findById(id).orElse(null);
        if(dossier == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        DossierDto dossierDto = new DossierDto(
                dossier.getRef(),
                dossier.getLabel(),
                dossier.getComments(),
                dossier.getCreatedAt(),
                dossier.getStartNoLaterThan(),
                dossier.getExpectCompletion(),
                new CodeDto(
                        dossier.getState().getCode().getCodeValue(),
                        dossier.getState().getCode().getLabelNl(),
                        dossier.getState().getCode().getLabelEn(),
                        dossier.getState().getCode().getLabelFr(),
                        dossier.getState().getCode().getLabelDe()
                ),
                new CodeDto(
                        dossier.getType().getCode().getCodeValue(),
                        dossier.getType().getCode().getLabelNl(),
                        dossier.getType().getCode().getLabelEn(),
                        dossier.getType().getCode().getLabelFr(),
                        dossier.getType().getCode().getLabelDe()
                )
        );

        return new ResponseEntity<>(Map.of("data", dossierDto), HttpStatus.OK);
    }

    @PatchMapping("dossier/{id}")
    public ResponseEntity<Map<String, Dossier>> updateDossierById(@PathVariable String id,
                                                                  @RequestBody PatchDossierDto dossierDto) {

        try {
            Dossier dossier = dossierRepository.findById(id).orElse(null);

            if(dossier == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if(dossierDto.label() != null) {
                dossier.setLabel(dossierDto.label());
            }

            if(dossierDto.comments() != null) {
                dossier.setComments(dossierDto.comments());
            }

            if(dossierDto.expectCompletion() != null) {
                if(
                    dossier.getStartNoLaterThan() != null &&
                    dossierDto.expectCompletion() != null &&
                    dossier.getStartNoLaterThan().after(dossierDto.expectCompletion())) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                dossier.setExpectCompletion(dossierDto.expectCompletion());
            }

            if(dossierDto.startNoLaterThan() != null) {
                if(
                    dossierDto.startNoLaterThan() != null &&
                    dossier.getExpectCompletion() != null &&
                    dossierDto.startNoLaterThan().after(dossier.getExpectCompletion())) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                dossier.setStartNoLaterThan(dossierDto.startNoLaterThan());
            }

            if(dossierDto.state() != null) {
                Code stateCode = codeRepository.findByCodeValueAndUsageContext(
                        dossierDto.state(), "DOSSIER_STATE")
                        .stream().findFirst().orElse(null);
                if(stateCode != null) {
                    DossierState state = dossier.getState();
                    state.setCode(stateCode);
                    dossierStateRepository.save(state);
                    dossier.setState(state);
                }
            }

            if(dossierDto.type() != null) {
                Code typeCode = codeRepository.findByCodeValueAndUsageContext(
                        dossierDto.type(), "DOSSIER_TYPE")
                        .stream().findFirst().orElse(null);
                if(typeCode != null) {
                    DossierType type = dossier.getType();
                    type.setCode(typeCode);
                    dossierTypeRepository.save(type);
                    dossier.setType(type);
                }
            }

            Dossier updated = dossierRepository.save(dossier);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "dossier/{id}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addDocumentToDossier(
        @PathVariable String id,
        @ModelAttribute AddDocumentToDossierDto addDocumentToDossier,
        @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        Dossier dossier = Utils.requireById(dossierRepository, id, "Dossier");
        DocumentType documentType =Utils.requireById(documentTypeRepository, addDocumentToDossier.documentTypeCode(), "DocumentType");
        DocumentState documentState = Utils.requireById(documentStateRepository, addDocumentToDossier.documentStateCode(), "DocumentState");
        DocumentRole documentRole = Utils.requireById(documentRoleRepository, addDocumentToDossier.documentRoleCode(), "DocumentRole");
        String localDocumentLabel = addDocumentToDossier.localDocumentLabel();
        String documentReferenceNotes = addDocumentToDossier.documentReferenceNotes();
        String CDIS = addDocumentToDossier.CDIS();
        String URI = addDocumentToDossier.URI();

        validateDocumentSource(CDIS, URI, file);

        DocumentReference documentReference = documentService.addDocument(
            documentType, 
            documentState, 
            localDocumentLabel,
            documentReferenceNotes, 
            CDIS,
            URI,
            file
        );

        DocumentRelatedSet documentRelatedSet = documentService.linkDocumentToDossier(
            documentReference, 
            dossier, 
            documentRole
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void validateDocumentSource(String cdis, String uri, MultipartFile file) {
        boolean hasCdis = cdis != null && !cdis.isEmpty();
        boolean hasUri = uri != null && !uri.isEmpty();
        boolean hasFile = file != null && !file.isEmpty();

        if (!hasCdis && !hasUri && !hasFile)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one of CDIS, URI or file must be provided.");

        if (hasCdis) {
            if (hasUri) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CDIS and URI cannot both be provided.");
            if (hasFile) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CDIS and file cannot both be provided.");
            if (!documentService.isValidCDIS(cdis)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CDIS format: " + cdis);
        } else if (hasUri) {
            if (hasFile) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URI and file cannot both be provided.");
            if (!documentService.isValidURI(uri)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid URI format: " + uri);
        } else if (hasFile && !documentService.isValidLocalDocument(file)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type. Only PNG, JPEG and PDF are allowed.");
        }
    }

    @GetMapping(value = "dossier/{id}/documents")
    public ResponseEntity<Page<DocumentListItemDto>> getDocumentsByDossier(@PathVariable String id, @PageableDefault(size = 20) Pageable pageable) {
        Dossier dossier = dossierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier not found with id: " + id));

        DocumentSet documentSet = documentSetRepository.findByBoundToDossier(dossier)
                .orElseThrow(() -> new RuntimeException("Document set not found for dossier with id: " + id));

        Page<DocumentRelatedSet> documentRelatedSetsPage = documentRelatedSetRepository.findByInSet(documentSet, pageable);

        Page<DocumentListItemDto> result = documentRelatedSetsPage
                .map(drs -> {
                    DocumentReference ref = drs.getDocument();
                    var localDoc = ref.getLocalDocument();

                    return new DocumentListItemDto(
                            drs.getRankInSet(),
                            drs.getRoleInSet().getCode(),

                            ref.getRef(),
                            ref.getType().getCode(),
                            ref.getNotes(),
                            ref.getIsDocLocal(),

                            localDoc != null ? localDoc.getLabel() : null,
                            localDoc != null ? localDoc.getState().getCode() : null
                    );
                });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping(value = "dossier/{dossier_id}/document/{document_id}")
    public ResponseEntity<Void> unlinkDocumentfromDossier(@PathVariable String dossier_id, @PathVariable Long document_id){
        Dossier dossier = dossierRepository.findById(dossier_id)
        .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Dossier not found with id: " + dossier_id
            )
        );

        DocumentReference docRef = documentReferenceRepository.findById(document_id)
        .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Document reference not found with id: " + document_id
            )
        );

        documentService.unlinkDocumentFromDossier(dossier, docRef);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}