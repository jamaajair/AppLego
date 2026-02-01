package org.spftech.backend.entity.Party;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;
import org.spftech.backend.entity.Code;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ParticipantRole{
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_role_id", referencedColumnName = "id", nullable = false)
    private Code code;

    public ParticipantRole(Code code){
        this.code = code;
    }
}