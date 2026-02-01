package org.spftech.backend.controller;

import lombok.RequiredArgsConstructor;

import org.spftech.backend.entity.*;
import org.spftech.backend.repository.*;
import org.spftech.backend.dto.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.annotation.Id;
import org.springframework.dao.DataAccessException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/")
public class DossierController{
    private final DossierRepository dossierRepository;
    private final DossierTypeRepository dossierTypeRepository;
    private final DossierStateRepository dossierStateRepository;
    private final CodeRepository codeRepository;

    @PostMapping("dossier/new")
    public ResponseEntity<Map<String, Dossier>> createDossier(@RequestBody NewDossierDto dossierDto){
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
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
        if(saved == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Map.of("data", saved), HttpStatus.CREATED);
    }

    @GetMapping("my_dossiers")
    public ResponseEntity<Map<String, List<DossierDto>>> getAllDossiers() {
        try {
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
}