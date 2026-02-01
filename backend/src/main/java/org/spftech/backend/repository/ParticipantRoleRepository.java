package org.spftech.backend.repository;

import org.spftech.backend.entity.Party.ParticipantRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ParticipantRoleRepository extends JpaRepository<ParticipantRole, UUID> {
}
