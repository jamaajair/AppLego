package org.spftech.backend.entity.Document;
import org.spftech.backend.entity.Asset;
import org.spftech.backend.entity.Dossier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
public class DocumentSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref")
    private Long ref;

    @Column(length = 250)
    private String label;

    @ManyToOne
    @JoinColumn(name = "binding", referencedColumnName = "code")
    private DocumentSetBinding binding;

    @ManyToOne
    @JoinColumn(name = "bound_to_asset", referencedColumnName = "id")
    private Asset boundToAsset;
    
    @ManyToOne
    @JoinColumn(name = "bound_to_dossier", referencedColumnName = "ref")
    private Dossier boundToDossier;


}