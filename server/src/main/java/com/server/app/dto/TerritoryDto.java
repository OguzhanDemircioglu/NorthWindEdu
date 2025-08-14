package com.server.app.dto;

import com.server.app.model.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryDto {
    private String territoryId;
    private String territoryDescription;
    private Short regionId;
}