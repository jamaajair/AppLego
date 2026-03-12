package org.spftech.backend.ruleunit;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.spftech.backend.entity.Party.ParticipantRole;

import java.util.ArrayList;
import java.util.List;

/**
 * RuleUnit for group 2: validates that the role has the correct usageContext
 * and is one of the seeded roles (ROLE_REQ, ROLE_EXP, ROLE_ADV).
 * Only needs a ParticipantRole — no dossier data required at this stage.
 */
public class RoleValidationUnit implements ValidationRuleUnit {

    public static final String GROUP = "valid-participant-role";

    private final DataStore<ParticipantRole> roles = DataSource.createStore();
    private final List<String> validationMessages = new ArrayList<>();

    public RoleValidationUnit(ParticipantRole role) {
        roles.add(role);
    }

    public DataStore<ParticipantRole> getRoles() {
        return roles;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }
}
