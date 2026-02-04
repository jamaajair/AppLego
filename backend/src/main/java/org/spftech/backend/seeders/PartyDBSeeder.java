package org.spftech.backend.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;

import org.spftech.backend.entity.*;
import org.spftech.backend.entity.Party.*;

import org.spftech.backend.repository.*;

@Component
@Order(2)
public class PartyDBSeeder implements CommandLineRunner {

    @Autowired
    private PartyRepository partyRepository;

    @Override
    public void run(String... args) throws Exception {
        // partyRepository.deleteAll();

        List<Party> parties = Arrays.asList(
            new Party(null, "Jamaa JAIR", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "85.01.15-123.45", null),
            new Party(null, "JAIR Solutions", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0123456789"),
            new Party(null, "Sarah Dupont", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "92.04.25-111.22", null),
            new Party(null, "TechNova SRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0456123456"),
            new Party(null, "Lucas Martin", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "90.07.10-456.78", null),
            new Party(null, "GreenPower SA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0999888777"),
            new Party(null, "Emma Leroy", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "94.11.23-999.00", null),
            new Party(null, "DataLink SPRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0666555444"),
            new Party(null, "Noah Bernard", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "87.06.05-222.33", null),
            new Party(null, "SmartCorp SA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0123987654"),
            new Party(null, "Julie Lambert", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "95.02.14-543.21", null),
            new Party(null, "EcoTrans SPRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0777123456"),
            new Party(null, "Thomas Girard", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "93.03.10-765.43", null),
            new Party(null, "BlueSky SRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0555999888"),
            new Party(null, "Laura Petit", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "91.12.31-333.44", null),
            new Party(null, "InnovateTech BVBA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0444333222"),
            new Party(null, "Marc Dupuis", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "88.09.17-888.77", null),
            new Party(null, "GlobeTrade SA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0222111000"),
            new Party(null, "Sophie Mercier", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "96.05.20-666.55", null),
            new Party(null, "QuantumSoft SPRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0888777666"),
            new Party(null, "Antoine Rousseau", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "89.08.12-444.33", null),
            new Party(null, "Eco Innovations", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0333222111"),
            new Party(null, "Claire Bertrand", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "97.01.30-222.11", null),
            new Party(null, "LogicSphere BVBA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0777666555"),
            new Party(null, "Pierre Moreau", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "86.04.05-999.88", null),
            new Party(null, "AgileGroup SRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0555444333"),
            new Party(null, "Amélie Lefebvre", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "92.11.08-777.66", null),
            new Party(null, "NexusTech SA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0444333222"),
            new Party(null, "Romain Giroux", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "95.06.22-555.44", null),
            new Party(null, "SolarEdge SPRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0888777666"),
            new Party(null, "Léa Dupont", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "90.03.15-333.22", null),
            new Party(null, "ConnectWorld BVBA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0222111000"),
            new Party(null, "Julien Martin", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "93.09.01-666.55", null),
            new Party(null, "StrategyLink SA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0777666555"),
            new Party(null, "Marie Roussel", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "96.12.25-444.33", null),
            new Party(null, "IntelliSoft SPRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0555444333"),
            new Party(null, "Nicolas Bernard", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "89.07.14-888.77", null),
            new Party(null, "CyberNetic SRL", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0333222111"),
            new Party(null, "Camille Leroy", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "94.02.19-222.11", null),
            new Party(null, "DynamicEdge BVBA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0444333222"),
            new Party(null, "Alexandre Petit", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "91.05.07-999.88", null),
            new Party(null, "FutureTech SA", PartyKind.MORAL_PERSON, DesignationMethod.legal_representative, null, "BE0888777666"),
            new Party(null, "Chloé Mercier", PartyKind.PHYSICAL_PERSON, DesignationMethod.in_person, "97.10.30-555.44", null)
        );

        for (Party p : parties) {
            boolean exists = partyRepository.existsByLabel(p.getLabel());
            if (!exists) {
                partyRepository.save(p);
        }
}

    }
}
