package org.spftech.backend.entity;

import java.util.List;

import org.spftech.backend.entity.Party.*;
import org.spftech.backend.entity.*;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DossierRelatedParty{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dossier_ref", referencedColumnName = "ref")
    private Dossier dossier;

    @ManyToOne
    @JoinColumn(name = "party_ref", referencedColumnName = "ref")
    private Party party;

    @ManyToOne
    @JoinColumn(name = "participant_role_id", referencedColumnName = "id")
    private ParticipantRole participantRole;
    
    // Need to deal with one dossier - multiple parties relation
    // Accountability => responsibility de stakeholder

    // Involvement => degre d implication
}