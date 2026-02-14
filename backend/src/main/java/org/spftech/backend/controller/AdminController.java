package org.spftech.backend.controller;

import lombok.RequiredArgsConstructor;

import org.spftech.backend.entity.*;
import org.spftech.backend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
public class AdminController{
    private final DossierRepository dossierRepository;
    private final DossierRelatedCollaboratorRepository drcRepository;
    private final CollaboratorRepository collaboratorRepository;

    public static record drcRequest(String DossierRef, String CollaboratorRef) {}

    @GetMapping("dossiers")
    public ResponseEntity<Map<String, List<Dossier>>> getAllDossiers() {
        List<Dossier> dossiers = dossierRepository.findAll();
        return new ResponseEntity<>(Map.of("data", dossiers), HttpStatus.OK);
    }

    // @GetMapping("dossier/{ref}")
    // public ResponseEntity<Dossier> getDossier(@PathVariable String ref) {
    //     Dossier dossier = dossierRepository.findById(ref).orElseThrow(() -> new EntityNotFoundException());
    //     return new ResponseEntity<>(dossier, HttpStatus.OK);
    // }

    @GetMapping("collaborators")
    public ResponseEntity<Map<String, List<Collaborator>>> getAllCollaborators() {
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        return new ResponseEntity<>(Map.of("data", collaborators), HttpStatus.OK);
    }

    @GetMapping("dossiersRelatedCollabs")
    public ResponseEntity<Map<String, List<DossierRelatedCollaborator>>> getAllDossiersRelatedCollabs() {
        List<DossierRelatedCollaborator> drcs = drcRepository.findAll();
        return new ResponseEntity<>(Map.of("data", drcs), HttpStatus.OK);
    }

    @DeleteMapping("dossierRelatedCollaborator/{id}")
    public ResponseEntity<String> deleteDossierRelatedCollaborator(@PathVariable Long id){
        if(drcRepository.findById(id).isEmpty())
            return ResponseEntity.badRequest().body("Dossier Related Collaborator not found with id: " + id);
        drcRepository.deleteById(id);
        return ResponseEntity.ok("Dossier Related Collaborator deleted");
    }


    @PostMapping("dossierRelatedCollaborator")
    public ResponseEntity<DossierRelatedCollaborator> createDossierRelatedCollab(@RequestBody DossierRelatedCollaborator request){

        if(request.getCollaborator() == null || request.getCollaborator().getRef() == null){
            return ResponseEntity.badRequest().body(null);
        }

        if(request.getDossier() == null || request.getDossier().getRef() == null){
            return ResponseEntity.badRequest().body(null);
        }

        String dossierRef = request.getDossier().getRef();
        Dossier dossier = dossierRepository.findById(dossierRef).orElseThrow(() -> new EntityNotFoundException("Dossier not found with ref: " + dossierRef));

        Long collabRef = request.getCollaborator().getRef();
        Collaborator collab = collaboratorRepository.findById(collabRef).orElseThrow(() -> new EntityNotFoundException("Collaborator not found with ref: " + collabRef));
        
        if(request.getFromDate() == null){
            Calendar cal = Calendar.getInstance();
            request.setFromDate(cal.getTime());
        }
        Date fromDate = request.getFromDate();

        if(request.getToDate() == null){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 90);
            request.setToDate(cal.getTime());
        }
        Date toDate = request.getToDate();

        DossierRelatedCollaborator drc = new DossierRelatedCollaborator(collab, dossier, fromDate, toDate);
        DossierRelatedCollaborator drcSaved = drcRepository.save(drc);
        return ResponseEntity.ok(drcSaved);
    }
    
}