package org.spftech.backend.controller;

import java.util.List;

import org.spftech.backend.entity.Party.*;
import org.spftech.backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/dossiers/parties")
public class PartyController {
    @Autowired 
    private PartyRepository repo;

    @PostMapping
    public Party create(@RequestBody Party p) {
        return repo.save(p);
    }

    @GetMapping
    public List<Party> getAll() {
        return repo.findAll();
    }
}
