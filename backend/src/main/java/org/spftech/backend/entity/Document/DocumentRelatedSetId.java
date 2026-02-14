package org.spftech.backend.entity.Document;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentRelatedSetId implements Serializable {

    @Column(name = "document")
    private Long document;

    @Column(name = "in_set")
    private Long inSet;
}
