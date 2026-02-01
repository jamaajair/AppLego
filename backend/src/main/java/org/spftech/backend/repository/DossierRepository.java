package org.spftech.backend.repository;

import org.spftech.backend.entity.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface DossierRepository extends JpaRepository<Dossier, String> {
    @Override
    @EntityGraph(attributePaths = {"type", "state"})
    List<Dossier> findAll();

    boolean existsByRef(String ref);
}