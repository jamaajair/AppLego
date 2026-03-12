package org.spftech.backend.ruleunit;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.spftech.backend.entity.Dossier;

import java.util.ArrayList;
import java.util.List;

/**
 * RuleUnit for group 1: validates that the dossier is in Active state.
 * Only needs a Dossier — no role data required at this stage.
 */
public class DossierStateValidationUnit implements ValidationRuleUnit {

    public static final String GROUP = "valid-dossier-state";

    private final DataStore<Dossier> dossiers = DataSource.createStore();
    private final List<String> validationMessages = new ArrayList<>();

    public DossierStateValidationUnit(Dossier dossier) {
        dossiers.add(dossier);
    }

    public DataStore<Dossier> getDossiers() {
        return dossiers;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }
}
