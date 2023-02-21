package com.cpp.devops.exception;

import lombok.Data;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-14 18:05
 **/
@Data
public class BusinessException extends RuntimeException {

    private static final long   serialVersionUID = 1L;
    private              int    code;
    private              String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
