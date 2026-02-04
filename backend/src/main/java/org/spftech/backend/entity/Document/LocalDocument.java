package org.spftech.backend.entity.Document;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@AllArgsConstructor
@Table(name = "local_document")
@Entity
@NoArgsConstructor
public class LocalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref")
    private Long ref;

    @OneToOne(mappedBy = "local_Document", cascade = CascadeType.ALL, optional = false)
    private DocumentReference documentReference;

    @Column(name = "label", length = 250, nullable = false)
    private String label;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "code", nullable = false)
    private DocumentType type;

    @Column(name = "published_date", nullable = false)
    private Date published;

    @ManyToOne
    @JoinColumn(name = "state", referencedColumnName = "code")
    private DocumentState state;

    @Column(name = "archived_as_doc_ref")
    private short archived_as_docRef; 
}