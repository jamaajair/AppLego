package org.spftech.backend.entity;

import jakarta.persistence.*;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Getter
public class DossierRelatedCollaborator{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "collaborator", referencedColumnName = "ref")
    private Collaborator collaborator;

    @ManyToOne
    @JoinColumn(name = "dossier", referencedColumnName = "ref")
    private Dossier dossier;

    // @ManyToOne
    // @JoinColumn(name = "required_competence", referencedColumnName = "code")
    // private CompentenceCode requiredCompetence;

    // @ManyToOne
    // @JoinColumn(name = "accountability", referencedColumnName = "code")
    // private AccountabilityCode accountability;

    @Column
    private Date fromDate;

    @Column
    private Date toDate;

    public DossierRelatedCollaborator(Collaborator collaborator, Dossier dossier, Date fromDate, Date toDate){
        this.collaborator = collaborator;
        this.dossier = dossier;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}