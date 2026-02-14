package org.spftech.backend.entity.Document;
import org.spftech.backend.entity.Dossier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@Table(name = "document_set")
@Data
@NoArgsConstructor
public class DocumentSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref")
    private Long ref;

    @Column(name = "label", length = 250)
    private String label;

    // BINDING

    // BOUND TO ASSET

    @ManyToOne
    @JoinColumn(name = "bound_to_dossier", referencedColumnName = "ref")
    private Dossier boundToDossier;


}