package com.cpp.devops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpp.devops.entity.User;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-09-07 14:46:08
 */
public interface UserService extends IService<User> {

    List<String> emailToUserId(List<String> emailList);

    void syncUsers() throws Exception;

    User getByEmail(String email);
}

