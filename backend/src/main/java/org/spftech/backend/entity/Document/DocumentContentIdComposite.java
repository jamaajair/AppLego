package org.spftech.backend.entity.Document;


import java.io.Serializable;

import jakarta.annotation.Generated;
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
public class DocumentContentIdComposite implements Serializable{
    
    
    @Column(name = "DOC_REF")
    private Long documentN;

    // @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "PART_NUMBER")
    private Integer part_number;
}
