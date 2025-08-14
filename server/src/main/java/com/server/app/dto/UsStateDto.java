package com.server.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsStateDto {
    private Short stateId;
    private String stateName;
    private String stateAbbr;
    private String stateRegion;
}
