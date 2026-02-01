package org.spftech.backend.repository;

import org.spftech.backend.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CodeRepository extends JpaRepository<Code, UUID> {
    List<Code> findByUsageContext(String usageContext);
    List<Code> findByCodeValueAndUsageContext(String codeValue, String usageContext);
    List<Code> findByCodeValueAndUsageContextAndDeactivated(String codeValue, String usageContext, boolean deactivated);
    boolean existsByCodeValueAndUsageContext(String codeValue, String usageContext);
}