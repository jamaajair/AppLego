package org.spftech.backend.entity.Document;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_reference")
@Data
@NoArgsConstructor
public class DocumentReference {
    
    @Id
    @Column(name = "ref")
    private Long ref;

    @MapsId 
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ref")
    private LocalDocument local_Document;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "code")
    private DocumentType document_type;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "id_doc_cdis", length = 100)
    private String id_doc_cdis;

    @Column(name = "id_doc_uri", length = 500)
    private String id_coc_uri;

    @Column(name = "is_doc_local")
    private Short is_doc_local;
}