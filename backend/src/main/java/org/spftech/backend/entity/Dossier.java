package org.spftech.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Dossier {

    @Id
    @Column(length = 50)
    private String ref;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private DossierType type;

    @Column(length = 250)
    private String label;

    @Column(nullable = false)
    private Date createdAt;

    @Column(length = 4000)
    private String comments;

    @ManyToOne
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private DossierState state;

    @Column(nullable = true)
    private Date startNoLaterThan;

    @Column(nullable = true)
    private Date expectCompletion;

}