package com.server.app.dto.request.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionUpdateRequest {
    private Long regionId;
    private String regionDescription;
}
