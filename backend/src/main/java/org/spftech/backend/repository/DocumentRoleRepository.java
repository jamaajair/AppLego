package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRoleRepository extends JpaRepository<DocumentRole, String> {
	
}
