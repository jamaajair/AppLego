package org.spftech.backend.entity.Party;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "party")
public class Party {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ref;

    @Column(length = 100)
    private String label;

    @Enumerated(EnumType.STRING)
    private PartyKind kind;

    @Enumerated(EnumType.STRING)
    private DesignationMethod designationMethod;

    @Column(length = 100)
    private String nationalNumber;

    @Column(length = 100)
    private String companyRegistrationNumber;

    // @Column(length = 10)
    // private String idQualifier;
    
    // @Column(length = 100)
    // private String idValue;
}
