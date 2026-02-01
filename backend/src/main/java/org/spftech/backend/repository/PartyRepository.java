package org.spftech.backend.repository;

import org.spftech.backend.entity.Party.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
    boolean existsByLabel(String label);    
}
