package com.server.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.server.app.model.Region;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryUpdateRequest {
    private String territoryId;
    private String territoryDescription;
    private Short regionId;
}
