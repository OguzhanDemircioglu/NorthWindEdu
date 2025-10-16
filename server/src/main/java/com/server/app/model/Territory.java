package com.server.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "territories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Territory {

    @Id
    @SequenceGenerator(name = "territory_seq", sequenceName = "territory_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "territory_seq")
    @Column(name = "territory_id", nullable = false, unique = true, updatable = false)
    private Long territoryId;

    @Column(name = "territory_description", columnDefinition = "bpchar", nullable = false)
    private String territoryDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}
