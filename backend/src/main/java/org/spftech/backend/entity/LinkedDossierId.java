package org.spftech.backend.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Embeddable
public class LinkedDossierId implements Serializable{
    
    @Column(name= "parent_dossier_id", length= 50)
    private String parentDossierId;

    @Column(name= "child_dossier_id", length= 50)
    private String childDossierId;

    public LinkedDossierId() {}

    public LinkedDossierId(String parentDossierId, String childDosseirId) {
        this.parentDossierId = parentDossierId;
        this.childDossierId = childDosseirId;
    }
}
