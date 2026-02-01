package org.spftech.backend.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.spftech.backend.entity.Party.ParticipantRole;
import org.spftech.backend.repository.ParticipantRoleRepository;
import org.spftech.backend.entity.Code;
import org.spftech.backend.repository.CodeRepository;

@Component
public class RolesDBSeeder implements CommandLineRunner {
    @Autowired
    private ParticipantRoleRepository participantRoleRepository;

    @Autowired
    private CodeRepository codeRepository;

    public void run(String... args)throws Exception{
        if(codeRepository.findByUsageContext("PARTICIPANT_ROLE").size() > 0){
            System.out.println("Participant roles already seeded.");
            return;
        } else {
            Code roleReq = new Code(
                    "ROLE_REQ",
                    "Aanvrager",
                    "Requestor",
                    "Demandeur",
                    "Antragsteller",
                    "PARTICIPANT_ROLE"
            );

            Code roleExp = new Code(
                    "ROLE_EXP",
                    "Deskundige",
                    "Expert",
                    "Expert",
                    "Experte",
                    "PARTICIPANT_ROLE"
            );

            Code roleAdv = new Code(
                    "ROLE_ADV",
                    "Adviseur",
                    "Advisor",
                    "Conseiller",
                    "Berater",
                    "PARTICIPANT_ROLE"
            );

            codeRepository.save(roleAdv);
            codeRepository.save(roleReq);
            codeRepository.save(roleExp);

            System.out.println("Participant roles seeded.");

        }}
}