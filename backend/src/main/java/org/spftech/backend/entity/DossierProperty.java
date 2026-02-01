package org.spftech.backend.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DossierProperty{

    @Id
    @Column(length=10, nullable=false)
    private String id;

    @Column(length=15, nullable=true)
    private String label;

    @Column(length=15, nullable=true)
    private String type;

    @ManyToOne
    @JoinColumn(name="dossier_ref", referencedColumnName="ref")
    private Dossier dossier;

    @Lob
    @Column(nullable = true)
    private String value;

    private Short isComposite;

    private int rankInComposite;

    @ManyToOne
    @JoinColumn(name="parent_property_id", referencedColumnName="id")
    private DossierProperty parentProperty;

}