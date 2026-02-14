package org.spftech.backend.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ref;

    @Column(length = 250)
    private String name;

    // @ManyToOne
    // @JoinColumn(name = "kind", referencedColumnName = "code")
    // private CollaboratorKind kind;

    @Column(length = 50)
    private String userId;

    public Collaborator(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }
}