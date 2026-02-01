package org.spftech.backend.repository;

import org.spftech.backend.entity.DossierState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DossierStateRepository extends JpaRepository<DossierState, UUID> {
}