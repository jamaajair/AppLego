package org.spftech.backend.entity.Document;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
    private DocumentContentId id;

    @MapsId("docRef")
    @ManyToOne
    @JoinColumn(name = "doc_ref", referencedColumnName = "ref")
    private LocalDocument docRef;

    @Column(name = "iana_content_type", length = 200)
    private String ianaContentType;

    @Lob
    @Column(name="content", columnDefinition = "LONGTEXT")
    private String content;
}
