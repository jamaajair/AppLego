package org.spftech.backend.entity.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class DocumentReference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref")
    private Long ref;

    @OneToOne(optional = true)
    @JoinColumn(name = "local_document_ref", referencedColumnName = "ref", nullable = true, unique = true)
    private LocalDocument localDocument;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "code")
    private DocumentType type;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(length = 100)
    private String idDocCdis;

    @Column(length = 500)
    private String idDocUri;

    @Column(name = "is_doc_local")
    private Short isDocLocal;
}