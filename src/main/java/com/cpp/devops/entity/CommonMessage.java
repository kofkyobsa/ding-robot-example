package com.cpp.devops.entity;

import lombok.Data;

import java.util.Set;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-11-18 09:45
 **/
@Data
public class CommonMessage {

    private Set<String> emails;
    private String      title;
    private String      message;
}
