package org.spftech.backend.repository;

import org.spftech.backend.entity.DossierType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DossierTypeRepository extends JpaRepository<DossierType, UUID> {
}
