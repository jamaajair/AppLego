package org.spftech.backend.ruleunit;

import org.drools.ruleunits.api.RuleUnitProvider;
import org.spftech.backend.entity.Dossier;
import org.spftech.backend.entity.Party.ParticipantRole;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ValidationContext {

    private final Map<String, ValidationRuleUnit> units = new LinkedHashMap<>();

    public ValidationContext with(Dossier dossier) {
        register(DossierStateValidationUnit.GROUP, new DossierStateValidationUnit(dossier));
        return this;
    }

    public ValidationContext with(ParticipantRole role) {
        register(RoleValidationUnit.GROUP,      new RoleValidationUnit(role));
        register(ExpertRoleValidationUnit.GROUP, new ExpertRoleValidationUnit(role));
        return this;
    }

    private void register(String groupName, ValidationRuleUnit unit) {
        units.put(groupName, unit);
    }

    public void fireGroups(List<String> groups) {
        for (String group : groups) {
            ValidationRuleUnit unit = units.get(group);
            if (unit == null) {
                throw new IllegalArgumentException("Unknown validation group: " + group);
            }
            try (var instance = RuleUnitProvider.get().createRuleUnitInstance(unit)) {
                instance.fire();
            }
            if (!unit.getValidationMessages().isEmpty()) {
                throw new RuntimeException("Validation failed [" + group + "]: "
                        + String.join(", ", unit.getValidationMessages()));
            }
        }
    }

}
