package com.server.app.dto.request.territory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerritorySaveRequest {
    private String territoryId;
    private String territoryDescription;
    private Long regionId;
}
