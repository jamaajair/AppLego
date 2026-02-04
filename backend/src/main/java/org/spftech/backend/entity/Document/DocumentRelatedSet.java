package org.spftech.backend.entity.Document;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "document_related_set")
@Data
@NoArgsConstructor
public class DocumentRelatedSet {

    @EmbeddedId
    private DocumentRelatedSetIdComposite id;

    // @Column(name = "document_n")
    // private Long documentN;
    @MapsId("documentN")
    @ManyToOne
    @JoinColumn(name = "DOCUMENT", referencedColumnName = "ref")
    private DocumentReference document;

    // @Column(name = "in_set_n")
    // private Long inSetN;
    @MapsId("inSetN")
    @ManyToOne
    @JoinColumn(name = "IN_SET", referencedColumnName = "ref")
    private DocumentSet documentSet;

    @ManyToOne
    @JoinColumn(name = "role", referencedColumnName = "code")
    private DocumentRole role;

    @Column(name = "rank_in_sft")
    private Integer rank;
}
