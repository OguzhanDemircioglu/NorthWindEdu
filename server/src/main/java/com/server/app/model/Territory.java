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
    @Column(name = "territory_id", columnDefinition = "varchar",length = 20, nullable = false, unique = true, updatable = false)
    private String territoryId;

    @Column(name = "territory_description", columnDefinition = "bpchar", nullable = false)
    private String territoryDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}
