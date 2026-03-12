package org.spftech.backend.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.spftech.backend.entity.Dossier;
import org.spftech.backend.entity.DossierRelatedParty;
import org.spftech.backend.entity.Party.Party;
import org.spftech.backend.entity.Party.ParticipantRole;
import org.spftech.backend.repository.DossierRepository;
import org.spftech.backend.repository.PartyRepository;
import org.spftech.backend.repository.ParticipantRoleRepository;
import org.spftech.backend.repository.DossierRelatedPartyRepository;
import org.spftech.backend.repository.CodeRepository;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DossierRelatedPartyServiceTest {

    @Autowired
    private DossierRelatedPartyService service;

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private ParticipantRoleRepository participantRoleRepository;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private DossierRelatedPartyRepository relatedPartyRepository;

    // Cache seeded data to avoid repeated queries
    private Dossier newDossier;
    private Dossier activeDossier;
    private Party testParty;
    private ParticipantRole expertRole;
    private ParticipantRole requestorRole;
    private boolean dataInitialized = false;

    @BeforeEach
    void setUp() {
        if (!dataInitialized) {
            // Find seeded dossiers by state
            newDossier = dossierRepository.findAll().stream()
                    .filter(d -> d.getState().getCode().getCodeValue().equals("N"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No new dossier found in seeded data"));

            activeDossier = dossierRepository.findAll().stream()
                    .filter(d -> d.getState().getCode().getCodeValue().equals("A"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No active dossier found in seeded data"));

            // Find first party
            testParty = partyRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No parties found in seeded data"));

            // Find seeded roles by creating ParticipantRole entities from codes
            expertRole = new ParticipantRole(codeRepository.findByCodeValueAndUsageContext("ROLE_EXP", "PARTICIPANT_ROLE")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("ROLE_EXP code not found in seeded data")));

            requestorRole = new ParticipantRole(codeRepository.findByCodeValueAndUsageContext("ROLE_REQ", "PARTICIPANT_ROLE")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("ROLE_REQ code not found in seeded data")));

            dataInitialized = true;
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should create relationship when all validations pass")
    void testLinkPartyWithRules_Success() {
        // Use seeded data
        DossierRelatedParty result = service.linkPartyToDossierWithRules(
            newDossier.getRef(), testParty.getRef(), "ROLE_EXP",
            List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp"));

        assertNotNull(result);
        assertEquals(newDossier, result.getDossier());
        assertEquals(testParty, result.getParty());
        assertEquals("ROLE_EXP", result.getParticipantRole().getCode().getCodeValue());
    }

    @Test
    @Order(2)
    @DisplayName("Should fail when dossier is not in New state")
    void testLinkPartyWithRules_InvalidDossierState() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.linkPartyToDossierWithRules(
                activeDossier.getRef(), testParty.getRef(), "ROLE_EXP",
                List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp"));
        });

        assertTrue(exception.getMessage().contains("Dossier must be in 'New' state"));
    }

    @Test
    @Order(3)
    @DisplayName("Should fail when non-expert role is used")
    void testLinkPartyWithRules_NonExpertRole() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.linkPartyToDossierWithRules(
                newDossier.getRef(), testParty.getRef(), "ROLE_REQ",
                List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp"));
        });

        assertTrue(exception.getMessage().contains("Only expert roles (ROLE_EXP) are allowed"));
    }

    @Test
    @Order(4)
    @DisplayName("Should fail when relationship already exists")
    void testLinkPartyWithRules_DuplicateRelationship() {
        // Create initial relationship
        DossierRelatedParty existing = new DossierRelatedParty();
        existing.setDossier(newDossier);
        existing.setParty(testParty);
        relatedPartyRepository.save(existing);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.linkPartyToDossierWithRules(
                newDossier.getRef(), testParty.getRef(), "ROLE_EXP",
                List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp"));
        });

        assertTrue(exception.getMessage().contains("Relationship already exists"));
    }

    @Test
    @Order(5)
    @DisplayName("Should fail when dossier doesn't exist")
    void testLinkPartyWithRules_DossierNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.linkPartyToDossierWithRules(
                "NON-EXISTENT", testParty.getRef(), "ROLE_EXP",
                List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp"));
        });

        assertTrue(exception.getMessage().contains("Dossier non trouvé"));
    }

    @Test
    @Order(6)
    @DisplayName("Should fail when party doesn't exist")
    void testLinkPartyWithRules_PartyNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.linkPartyToDossierWithRules(
                activeDossier.getRef(), 999999999L, "ROLE_EXP",
                List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp"));
        });

        assertTrue(exception.getMessage().contains("Partie non trouvée"));
    }

    @Test
    @Order(7)
    @DisplayName("Should fail when role doesn't exist")
    void testLinkPartyWithRules_RoleNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.linkPartyToDossierWithRules(
                activeDossier.getRef(), testParty.getRef(), "NON_EXISTENT_ROLE",
                List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp"));
        });

        assertTrue(exception.getMessage().contains("Participant role not found"));
    }

    @Test
    @Order(8)
    @DisplayName("Partial selection: only dossier state checked — non-expert role passes")
    void testLinkPartyWithRules_PartialSelection_DossierOnly() {
        // ROLE_REQ would normally fail valid-participant-role-exp, but we don't fire it
        DossierRelatedParty result = service.linkPartyToDossierWithRules(
            newDossier.getRef(), testParty.getRef(), "ROLE_REQ",
            List.of("valid-dossier-state"));

        assertNotNull(result);
    }

    @Test
    @Order(9)
    @DisplayName("Partial selection: ROLE_ADV passes when only role structure is checked")
    void testLinkPartyWithRules_PartialSelection_AdvisorPassesRoleCheck() {
        // ROLE_ADV is a seeded role → passes valid-participant-role; expert check not fired
        assertDoesNotThrow(() -> service.linkPartyToDossierWithRules(
            newDossier.getRef(), testParty.getRef(), "ROLE_ADV",
            List.of("valid-dossier-state", "valid-participant-role")));
    }

    @Test
    @Order(10)
    @DisplayName("Partial selection: ROLE_ADV fails when expert check is included")
    void testLinkPartyWithRules_PartialSelection_AdvisorFailsExpertCheck() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.linkPartyToDossierWithRules(
            newDossier.getRef(), testParty.getRef(), "ROLE_ADV",
            List.of("valid-dossier-state", "valid-participant-role", "valid-participant-role-exp")));

        assertTrue(exception.getMessage().contains("Only expert roles (ROLE_EXP) are allowed"));
    }

    @Test
    @Order(11)
    @DisplayName("Partial selection: active dossier passes when dossier state check is skipped")
    void testLinkPartyWithRules_PartialSelection_SkipDossierCheck() {
        // Active dossier would fail valid-dossier-state, but we skip it
        DossierRelatedParty result = service.linkPartyToDossierWithRules(
            activeDossier.getRef(), testParty.getRef(), "ROLE_EXP",
            List.of("valid-participant-role", "valid-participant-role-exp"));

        assertNotNull(result);
    }
}
