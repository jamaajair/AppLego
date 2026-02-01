package org.spftech.backend.controller;

import org.spftech.backend.entity.Party.ParticipantRole;
import org.springframework.web.bind.annotation.*;

import org.spftech.backend.repository.ParticipantRoleRepository;
import org.spftech.backend.repository.CodeRepository;

import org.spftech.backend.dto.CodeDto;
import org.spftech.backend.service.CodeMapper;

import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController
@RequestMapping("/api/user/dossiers/participant-roles")
public class ParticipantRoleController {
    @Autowired
    private ParticipantRoleRepository repo;

    private CodeMapper codeMapper = new CodeMapper();

    @Autowired
    private CodeRepository codeRepo;

    @GetMapping
    public List<CodeDto> getAllRoles() {
        return
                codeRepo.findByUsageContext("PARTICIPANT_ROLE").stream()
                .map(code -> codeMapper.toDto(code))
                        .toList();
    }
}
