package org.spftech.backend.repository;

import org.spftech.backend.entity.DossierProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossierPropertyRepository extends JpaRepository<DossierProperty, String> {
}