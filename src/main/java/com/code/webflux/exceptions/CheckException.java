package com.code.webflux.exceptions;

import lombok.Data;

/**
 * Created by yankefei on 2018/12/2.
 */
@Data
public class CheckException extends RuntimeException {

    private String fieldName;

    private String fieldValue;

    public CheckException(String fieldName, String fieldValue){
        super();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
