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
@Data
@Entity
public class DocumentSetBinding implements CodeEntity {
    @Id
    @Column(length = 1, nullable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String labelNl;

    @Column(length = 100, nullable = false)
    private String labelEn;

    @Column(length = 100, nullable = false)
    private String labelFr;

    @Column(length = 100, nullable = false)
    private String labelDe;   
}
