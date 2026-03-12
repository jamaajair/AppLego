package org.spftech.backend.ruleunit;

import org.drools.ruleunits.api.RuleUnitData;

import java.util.List;

public interface ValidationRuleUnit extends RuleUnitData {
    List<String> getValidationMessages();
}
