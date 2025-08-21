package com.server.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TerritoryDto {
    private String territoryId;
    private String territoryDescription;
    private Long regionId;
}