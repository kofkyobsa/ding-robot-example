package com.cpp.devops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpp.devops.entity.Conversation;

/**
 * (Conversation)表服务接口
 *
 * @author makejava
 * @since 2022-09-09 13:44:12
 */
public interface ConversationService extends IService<Conversation> {

    Conversation getByName(String name);
}

