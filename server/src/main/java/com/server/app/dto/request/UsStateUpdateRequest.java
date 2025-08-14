package com.server.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsStateUpdateRequest {
    private Short stateId;
    private String stateName;
    private String stateAbbr;
    private String stateRegion;
}
