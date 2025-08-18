package com.server.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "us_states")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsState {

    @Id
    @SequenceGenerator(name = "state_seq", sequenceName = "state_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "state_seq")
    @Column(name = "state_id", nullable = false, updatable = false, unique = true)
    private Long stateId;

    @Column(name = "state_name", columnDefinition = "varchar", length = 100)
    private String stateName;
    @Column(name = "state_abbr", columnDefinition = "varchar", length = 2)
    private String stateAbbr;
    @Column(name = "state_region", columnDefinition = "varchar", length = 50)
    private String stateRegion;
}
