package org.spftech.backend.ruleunit;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.spftech.backend.entity.Party.ParticipantRole;

import java.util.ArrayList;
import java.util.List;

/**
 * RuleUnit for group 3: validates that the role is specifically ROLE_EXP.
 * Only reached after group 2 has confirmed the role is structurally valid.
 */
public class ExpertRoleValidationUnit implements ValidationRuleUnit {

    public static final String GROUP = "valid-participant-role-exp";

    private final DataStore<ParticipantRole> roles = DataSource.createStore();
    private final List<String> validationMessages = new ArrayList<>();

    public ExpertRoleValidationUnit(ParticipantRole role) {
        roles.add(role);
    }

    public DataStore<ParticipantRole> getRoles() {
        return roles;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }
}
