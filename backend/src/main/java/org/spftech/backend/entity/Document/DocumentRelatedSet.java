package org.spftech.backend.entity.Document;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class DocumentRelatedSet {

    @EmbeddedId
    private DocumentRelatedSetId id;

    @MapsId("document")
    @ManyToOne
    @JoinColumn(name = "document", referencedColumnName = "ref")
    private DocumentReference document;

    @MapsId("inSet")
    @ManyToOne
    @JoinColumn(name = "in_set", referencedColumnName = "ref")
    private DocumentSet inSet;

    @ManyToOne
    @JoinColumn(name = "role_in_set", referencedColumnName = "code")
    private DocumentRole roleInSet;

    @Column(name = "rank_in_set")
    private Integer rankInSet;
}
