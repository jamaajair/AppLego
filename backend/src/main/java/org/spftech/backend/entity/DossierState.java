package org.spftech.backend.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DossierState{
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_state_id", referencedColumnName = "id", nullable = false)
    private Code code;

    public DossierState(Code code){
        this.code = code;
    }
}