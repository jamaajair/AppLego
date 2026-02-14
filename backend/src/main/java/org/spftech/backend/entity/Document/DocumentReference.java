package org.spftech.backend.entity.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class DocumentReference {
    
    @Id
    @Column(name = "ref")
    private Long ref;

    @MapsId 
    @OneToOne
    @JoinColumn(name = "ref")
    private LocalDocument localDocument;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "code")
    private DocumentType type;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "id_doc_cdis", length = 100)
    private String idDocCdis;

    @Column(name = "id_doc_uri", length = 500)
    private String idDocUri;

    @Column(name = "is_doc_local")
    private Short isDocLocal;
}