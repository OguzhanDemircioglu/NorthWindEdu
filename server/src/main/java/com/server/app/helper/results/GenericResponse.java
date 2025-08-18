package com.server.app.helper.results;

import com.server.app.enums.ResultMessages;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class GenericResponse {
    @Builder.Default
    private boolean success = true;
    @Builder.Default
    private String message = ResultMessages.SUCCESS;
}