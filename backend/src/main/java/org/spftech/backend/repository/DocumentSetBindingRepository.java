package org.spftech.backend.repository;

import org.spftech.backend.entity.Document.DocumentSetBinding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentSetBindingRepository extends JpaRepository<DocumentSetBinding, String> {
}
