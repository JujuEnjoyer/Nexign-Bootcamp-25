package ru.tyufyakov.project.testprojectfornexign.taskOne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CDRNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "call_type")
    private String incomingOrOutgoing; // "01" - входящий, "02" - исходящий

    @Column(name = "caller")
    private long caller; // Номер того, кто звонит

    @Column(name = "receiver")
    private long receiver; // Номер того, кому звонят

    @Column(name = "start_call_time")
    private ZonedDateTime startCallTime;

    @Column(name = "end_call_time")
    private ZonedDateTime endCallTime;

}
