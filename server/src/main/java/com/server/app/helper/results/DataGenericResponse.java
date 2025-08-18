package com.server.app.helper.results;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "dataBuilder")
public class DataGenericResponse<T> extends GenericResponse {
    private T data;
}