package org.spftech.backend.repository;

import org.spftech.backend.entity.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE collaborator AUTO_INCREMENT=1", nativeQuery = true)
    void resetAutoIncr();
}