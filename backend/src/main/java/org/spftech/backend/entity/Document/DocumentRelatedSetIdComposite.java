package org.spftech.backend.entity.Document;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentRelatedSetIdComposite implements Serializable {

    @Column(name = "DOCUMENT")
    private Long documentN;

    @Column(name = "IN_SET")
    private Long inSetN;
}
