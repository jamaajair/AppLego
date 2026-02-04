package org.spftech.backend.entity.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Table(name = "document_type")
@Data
@Entity
public class DocumentType{
    
    @Id
    @Column(name = "code", length=15, nullable=false)
    private String code;

    @Column(name = "label_nl", length=100, nullable=false)
    private String labelNl;

    @Column(name = "label_en", length=100, nullable=false)
    private String labelEn;

    @Column(name = "label_fr", length=100, nullable=false)
    private String labelFr;

    @Column(name = "label_de", length=100, nullable=false)
    private String labelDe;

    @Column(name = "equivalent_cdis_doctype", length=15, nullable=false)
    private String equivalent_cdis_doctype;
}