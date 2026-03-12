package org.spftech.backend.entity.Document;

import org.spftech.backend.entity.common.CodeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "document_role")
@Data
@Entity
public class DocumentRole implements CodeEntity {

    @Id
    @Column(length=15)
    private String code;

    @Column(length=100)
    private String labelNl;

    @Column(length=100)
    private String labelEn;

    @Column(length=100)
    private String labelFr;

    @Column(length=100)
    private String labelDe;

    @Column(length=15)
    private String cdisDoctype;


    
}
