package org.spftech.backend.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Calendar;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.spftech.backend.entity.*;
import org.spftech.backend.entity.Document.DocumentRelatedSet;
import org.spftech.backend.repository.*;

@Component
@Order(1)
public class DossierDBSeeder implements CommandLineRunner {

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierTypeRepository dossierTypeRepository;

    @Autowired
    private DossierStateRepository dossierStateRepository;

    @Autowired
    private LinkKindRepository linkKindRepository;

    @Autowired
    private LinkedDossierRepository linkedDossierRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private DossierRelatedCollaboratorRepository dossierRelatedCollaboratorRepository;

    @Autowired
    private DocumentSetRepository documentSetRepository;

    @Autowired
    private DocumentRelatedSetRepository documentRelatedSetRepository;

    @Autowired
    private DocumentReferenceRepository documentReferenceRepository;

    @Autowired
    private DocumentContentRepository documentContentRepository;

    @Autowired
    private LocalDocumentRepository localDocumentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private DocumentStateRepository documentStateRepository;

    @Autowired
    private DocumentRoleRepository documentRoleRepository;

    @Autowired
    private DocumentSetBindingRepository documentSetBindingRepository;

    @Override
    public void run(String... args) throws Exception {

        if (dossierRepository.count() > 0) {
            System.out.println("ℹ Dossiers déjà existants, seeding ignoré.");
            return;
        }

        // ── Collaborateurs ──────────────────────────────────────────────────────
        List<String> users = Arrays.asList("Nathan", "Jamaa", "Pierre", "Jerome", "Alexandre");
        List<Collaborator> collaborators = new ArrayList<>();
        for (String name : users) {
            collaborators.add(new Collaborator(name, name + "Id"));
        }
        collaboratorRepository.saveAll(collaborators);

        // ── Codes types & états ─────────────────────────────────────────────────
        Code typeCode1 = new Code(
                "SIMP",
                "Eenvoudige nalatenschap",
                "Simple succession",
                "Succession simple",
                "Einfache Erbschaft",
                "DOSSIER_TYPE"
        );

        Code typeCode2 = new Code(
                "COMP",
                "Complexe nalatenschap",
                "Complex succession",
                "Succession complexe",
                "Komplexe Erbschaft",
                "DOSSIER_TYPE"
        );

        codeRepository.save(typeCode1);
        codeRepository.save(typeCode2);

        Code stateCode1 = new Code("A", "Afgebroken", "Aborted", "Annulé", "Abgebrochen", "DOSSIER_STATE");
        Code stateCode2 = new Code("N", "Nieuw", "New", "Nouveau", "Neu", "DOSSIER_STATE");
        codeRepository.save(stateCode1);
        codeRepository.save(stateCode2);

        // ── Dossiers ────────────────────────────────────────────────────────────
        List<String> subjects = Arrays.asList(
                "Jean Dupont", "Marie Dubois", "Baptiste De Smet", "Pieter Janssens", "Sophie Martin",
                "Anne Leroy", "Thomas Van Dam", "Claire Bernard", "Vincent Lambert", "Isabelle Mercier",
                "Erich Thielen", "Chantal Simon", "François Willems", "Katrien Maes", "Nicolas Devos",
                "Laurette Vandenberghe", "Gaston Lambert", "Hélène Petit", "Marc Verhaeghe", "Elke Claes",
                "Olivier Laurent", "Julie Simonet", "Stéphane Boulanger", "Katrijn Peeters", "Bernard Renaud",
                "Céline Roussel", "Wim Jacobs", "Annelies Moens", "Gérard Bertrand", "Hal Anne"
        );

        List<Dossier> dossiers = new ArrayList<>();

        for (int i = 1; i <= subjects.size(); i++) {
            String ref = String.format("SUC-%03d", i);
            String title = "Succession de " + subjects.get(i - 1);
            String description = "Dossier de succession.";

            Code type = (i % 2 == 0) ? typeCode1 : typeCode2;
            Code state = (i % 3 == 0) ? stateCode1 : stateCode2;

            DossierType dossierType = new DossierType(type);
            DossierState dossierState = new DossierState(state);

            dossierTypeRepository.save(dossierType);
            dossierStateRepository.save(dossierState);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -i * 7);
            Date dateCreated = cal.getTime();

            Calendar openCal = (Calendar) cal.clone();
            openCal.add(Calendar.DAY_OF_MONTH, 2);
            Date dateOpened = openCal.getTime();

            Calendar closeCal = (Calendar) cal.clone();
            closeCal.add(Calendar.DAY_OF_MONTH, (state == stateCode2) ? 30 + (i % 4) * 30 : 90);
            Date dateClosed = closeCal.getTime();

            dossiers.add(new Dossier(ref, dossierType, title, dateCreated, description, dossierState, dateOpened, dateClosed));
        }

        dossierRepository.saveAll(dossiers);

        // ── Collaborateurs liés aux dossiers ────────────────────────────────────
        List<DossierRelatedCollaborator> drcs = new ArrayList<>();
        for (int i = 0; i < dossiers.size(); i++) {
            Calendar cal = Calendar.getInstance();
            Date fromDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 90);
            Date toDate = cal.getTime();

            drcs.add(new DossierRelatedCollaborator(
                    collaborators.get(i % collaborators.size()),
                    dossiers.get(i),
                    fromDate,
                    toDate
            ));
        }
        dossierRelatedCollaboratorRepository.saveAll(drcs);

        // ── LinkKind & LinkedDossier ────────────────────────────────────────────
        Code linkKindCode = new Code(
                "CLONE",
                "gekloond door",
                "cloned by",
                "geklont von",
                "cloné par",
                "LINK_KIND"
        );
        codeRepository.save(linkKindCode);

        LinkKind linkKind = new LinkKind(linkKindCode);
        linkKindRepository.save(linkKind);

        linkedDossierRepository.save(new LinkedDossier(dossiers.get(0), dossiers.get(1), linkKind, 1));

        System.out.println("✔ 30 dossiers de succession ajoutés (Gestion du Patrimoine - SPF Finances).");
    }
}