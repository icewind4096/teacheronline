package com.windvalley.guli.service.base.exception;

import com.windvalley.guli.common.base.result.ResultCodeEnum;
import lombok.Data;

@Data
public class WindvalleyException extends RuntimeException {
    private Integer code;

    public WindvalleyException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public WindvalleyException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "WindvalleyException{" +
                "code=" + code +
                "message=" + this.getMessage() +
                '}';
    }
}
