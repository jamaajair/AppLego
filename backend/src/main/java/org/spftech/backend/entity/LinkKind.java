package org.spftech.backend.entity;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class LinkKind{
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", referencedColumnName = "id", nullable = false)
    private Code code;

    public LinkKind(Code code){
        this.code = code;
    }
}