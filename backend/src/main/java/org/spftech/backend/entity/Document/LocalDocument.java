package org.spftech.backend.entity.Document;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class LocalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref")
    private Long ref;

    @OneToOne(mappedBy = "localDocument", optional = true)
    private DocumentReference documentReference;

    @Column(length = 250)
    private String label;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "code")
    private DocumentType type;

    private Date published;

    @ManyToOne
    @JoinColumn(name = "state", referencedColumnName = "code")
    private DocumentState state;

    private short archivedAsDocRef; 
}