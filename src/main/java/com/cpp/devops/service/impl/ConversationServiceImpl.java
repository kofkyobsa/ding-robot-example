package com.cpp.devops.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpp.devops.entity.Conversation;
import com.cpp.devops.mapper.ConversationMapper;
import com.cpp.devops.service.ConversationService;
import org.springframework.stereotype.Service;

/**
 * (Conversation)表服务实现类
 *
 * @author makejava
 * @since 2022-09-09 13:44:12
 */
@Service("conversationService")
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation>
        implements ConversationService {

    @Override
    public Conversation getByName(String name) {
        return getOne(new QueryWrapper<Conversation>().eq("name", name));
    }
}

