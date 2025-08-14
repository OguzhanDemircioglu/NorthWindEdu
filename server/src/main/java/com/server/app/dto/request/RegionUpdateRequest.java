package com.server.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionUpdateRequest {
    private Short regionId;
    private String regionDescription;
}
