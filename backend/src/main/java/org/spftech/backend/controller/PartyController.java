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
import org.springframework.security.access.prepost.PreAuthorize;

//@todo: this code is NOT correct, it SHOULD NOT be like this, we need to have a service layer and not directly use the repository in the controller, but for now we will keep it simple and direct, we will refactor it ASAP.
@RestController
@RequestMapping("/api/user/dossiers/parties")
@PreAuthorize("hasAnyRole('EMPLOYEES', 'ADMIN', 'SUPERVISORS')")
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
