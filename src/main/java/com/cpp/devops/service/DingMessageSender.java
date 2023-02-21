package com.cpp.devops.service;

import com.alibaba.fastjson.JSON;
import com.cpp.devops.entity.Conversation;
import com.cpp.devops.entity.User;
import com.cpp.devops.service.extend.AzureRestApi;
import com.cpp.devops.service.extend.DingTalkOpenApi;
import com.dingtalk.api.response.OapiChatCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: ding-message-service
 * @description: 用于消息发送
 * @author: HuiZhong
 * @create: 2022-09-07 16:17
 **/
@Service
public class DingMessageSender {

    @Autowired
    private UserService         userService;
    @Autowired
    private DingTalkOpenApi     openApi;
    @Autowired
    private AzureRestApi        azureRestApi;
    @Autowired
    private ConversationService conversationService;

    public void sendByEmail(Collection<String> emailList, String title, String context) throws Exception {
        List<String> userIds = new ArrayList<>();
        for (String email : emailList) {
            User user = userService.getByEmail(email);
            if (user != null) {
                userIds.add(user.getUserid());
            }
        }
        Map<String, String> param = new HashMap<>();
        param.put("title", title);
        param.put("text", context);
        openApi.notifyMsg(userIds, "sampleMarkdown", JSON.toJSONString(param));
    }

    public void sendGroupMsg(String projectId, String title, String message) throws Exception {
        String projectName = azureRestApi.getProjectName(projectId);
        String chatName = projectName + "项目发布通知群";
        Conversation chat = conversationService.getByName(chatName);
        if (chat == null) {
            List<String> teams = azureRestApi.getTeams(projectId);
            List<String> emails = new ArrayList<>();
            for (String team : teams) {
                emails.addAll(azureRestApi.getEmails(team));
            }
            List<String> userIds = userService.emailToUserId(emails);
            OapiChatCreateResponse rsp = openApi.createChat(chatName, userIds);
            if (rsp.isSuccess()) {
                chat = new Conversation();
                chat.setOpenConversationId(rsp.getOpenConversationId());
                chat.setChatid(rsp.getChatid());
                chat.setName(chatName);
                conversationService.save(chat);
            }
        }
        Map<String, String> param = new HashMap<>();
        param.put("title", title);
        param.put("text", message);
        openApi.sendGroupMessage(chat.getOpenConversationId(), "sampleMarkdown", JSON.toJSONString(param));
    }
}
