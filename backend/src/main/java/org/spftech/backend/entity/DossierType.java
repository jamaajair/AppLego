package org.spftech.backend.entity;

import jakarta.persistence.*;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DossierType{

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", referencedColumnName = "id", nullable = false)
    private Code code;

    public DossierType(Code code) {
        this.code = code;
    }
}
