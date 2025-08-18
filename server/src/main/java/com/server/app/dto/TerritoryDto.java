package com.server.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryDto {
    private String territoryId;
    private String territoryDescription;
    private Long regionId;
}