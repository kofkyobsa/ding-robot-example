package com.cpp.devops.system;

import lombok.Data;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-14 18:08
 **/
@Data
public class Result<T> {

    private int    code;
    private String message;
    T result;
}
