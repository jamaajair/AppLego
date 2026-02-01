package org.spftech.backend.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Code{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="code_value", length=50, nullable=false)
    private String codeValue;

    @Column(name="usage_context", length=50, nullable=false)
    private String usageContext;

    @Column(length=100, nullable=false)
    private String labelFr;

    @Column(length=100, nullable=false)
    private String labelEn;

    @Column(length=100, nullable=false)
    private String labelNl;

    @Column(length=100, nullable=false)
    private String labelDe;

    @Column(nullable=false)
    private boolean deactivated = false;

    @Column(nullable=false)
    private Date creationDate = new Date();

    @Column(nullable=true)
    private Date deactivationDate;

    @Column(nullable=false)
    private boolean editable = false;

    public Code(String codeValue, String labelNl, String labelEn, String labelFr, String labelDe, String usageContext) {
        this.codeValue = codeValue;
        this.labelNl = labelNl;
        this.labelEn = labelEn;
        this.labelFr = labelFr;
        this.labelDe = labelDe;
        this.usageContext = usageContext;
        this.creationDate = new Date();
    }

    public void setCodeValue(String codeValue) {
        if (!this.editable) {
            throw new UnsupportedOperationException("This code is not editable.");
        }
        this.codeValue = codeValue;
    }
}
