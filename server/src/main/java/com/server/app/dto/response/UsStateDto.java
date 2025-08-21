package com.server.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsStateDto {
    private Long stateId;
    private String stateName;
    private String stateAbbr;
    private String stateRegion;
}
