package org.spftech.backend.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class LinkedDossier{

    @EmbeddedId
    private LinkedDossierId id;

    @ManyToOne
    @MapsId("parentDossierId")
    @JoinColumn(name= "parent_dossier_ref", referencedColumnName= "ref")
    private Dossier parentDossier;

    @ManyToOne
    @MapsId("childDossierId")
    @JoinColumn(name= "child_dossier_ref", referencedColumnName= "ref")
    private Dossier childDossier;

    private Integer sequenceNumber;

    @ManyToOne
    @JoinColumn(name= "link_kind_id", referencedColumnName= "id")
    private LinkKind linkKind;

    public LinkedDossier(Dossier parent, Dossier child, LinkKind linkKind, Integer seq){
        this.id = new LinkedDossierId(parent.getRef(), child.getRef());
        this.parentDossier = parent;
        this.childDossier = child;
        this.linkKind = linkKind;
        this.sequenceNumber = seq;
    }
}