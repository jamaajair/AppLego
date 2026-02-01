package org.spftech.backend.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Calendar;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.spftech.backend.entity.*;
import org.spftech.backend.repository.*;

@Component
public class DossierDBSeeder implements CommandLineRunner {

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierTypeRepository dossierTypeRepository;

    @Autowired
    private DossierStateRepository dossierStateRepository;

    @Autowired
    private CodeRepository codeRepository;

    @Override
    public void run(String... args) throws Exception {
        // codeRepository.deleteAll();
        // dossierRepository.deleteAll();
        // dossierTypeRepository.deleteAll();
        // dossierStateRepository.deleteAll();

        // Correction : l'ordre des paramètres attendu est (code, label_nl, label_en, label_fr, label_de, usage_context)
        // et le code est généralement présent dans les diagrammes uml (sinon, en utiliser un court pour l'exemple).
        Code typeCode1 = new Code(
            "SIMP",
            "Eenvoudige nalatenschap",   // label_nl
            "Simple succession",        // label_en
            "Succession simple",        // label_fr
            "Einfache Erbschaft",        // label_de
            "DOSSIER_TYPE"
        );

        Code typeCode2 = new Code(
            "COMP",              // code (court)
            "Complexe nalatenschap",    // label_nl
            "Complex succession",       // label_en
            "Succession complexe",      // label_fr
            "Komplexe Erbschaft",       // label_de
            "DOSSIER_TYPE"
        );

        codeRepository.save(typeCode1);
        codeRepository.save(typeCode2);

        Code stateCode1 = new Code("A", "Afgebroken", "Aborted", "Annulé", "Abgebrochen", "DOSSIER_STATE");
        Code stateCode2 = new Code("N", "Nieuw", "New", "Nouveau", "Neu", "DOSSIER_STATE");
        codeRepository.save(stateCode1);
        codeRepository.save(stateCode2);

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
            String name = subjects.get(i - 1);
            String title = "Succession de " + name;
            String description = "Dossier de succession.";

            Code type = (i % 2 == 0) ? typeCode1 : typeCode2;
            Code state = (i % 3 == 0) ? stateCode1 : stateCode2;

            DossierType dossierType = new DossierType(type);
            DossierState dossierState = new DossierState(state);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -i * 7);
            Date dateCreated = cal.getTime();

            Calendar openCal = (Calendar) cal.clone();
            openCal.add(Calendar.DAY_OF_MONTH, 2);
            Date dateOpened = openCal.getTime();

            Calendar closeCal = (Calendar) cal.clone();
            if (state == stateCode2) {
                closeCal.add(Calendar.DAY_OF_MONTH, 30 + (i % 4) * 30);
            } else {
                closeCal.add(Calendar.DAY_OF_MONTH, 90);
            }
            Date dateClosed = closeCal.getTime();

            Dossier d = new Dossier(
                ref,
                dossierType,
                title,
                dateCreated,
                description,
                dossierState,
                dateOpened,
                dateClosed
            );

            dossierTypeRepository.save(dossierType);
            dossierStateRepository.save(dossierState);

            dossiers.add(d);
        }

        dossierRepository.saveAll(dossiers);

        System.out.println("30 dossiers de succession ajoutés.");
    }
}