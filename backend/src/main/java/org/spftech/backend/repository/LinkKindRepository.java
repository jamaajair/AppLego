package org.spftech.backend.repository;

import java.util.List;
import java.util.UUID;

import org.spftech.backend.entity.Code;
import org.spftech.backend.entity.LinkKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LinkKindRepository extends JpaRepository<LinkKind, UUID> {
    List<LinkKind> findByCode(Code linkKindCode);

    @Query("SELECT lk FROM LinkKind lk JOIN FETCH lk.code")
    List<LinkKind> findAllWithCode();
}
