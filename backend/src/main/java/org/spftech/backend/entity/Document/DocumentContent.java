package org.spftech.backend.entity.Document;

import java.sql.Clob;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentContent {
    
    @EmbeddedId
    private DocumentContentIdComposite id;

    @MapsId("documentN")
    @ManyToOne
    @JoinColumn(name = "DOC_REF", referencedColumnName = "ref")
    private LocalDocument document;

    @Column(name = "IANA_CONTENT_TYPE", length = 200)
    private String iana_content_type;

    @Column(name = "CONTENT")
    private Clob content;

}
