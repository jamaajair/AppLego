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
public class DocumentContentId implements Serializable{
    
    
    @Column(name = "doc_ref")
    private Long docRef;

    @Column(name = "part_number")
    private Integer partNumber;
}
