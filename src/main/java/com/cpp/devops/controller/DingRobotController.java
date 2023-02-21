package com.cpp.devops.controller;

import com.alibaba.fastjson.JSONObject;
import com.cpp.devops.service.DingMessageSender;
import com.cpp.devops.service.extend.AzureRestApi;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-07 16:25
 **/
@Slf4j
@RestController
@RequestMapping("robot")
public class DingRobotController {

    @Autowired
    private DingMessageSender messageSender;

    @Autowired
    private AzureRestApi azureRestApi;

    @Value("${ding.debug:false}")
    private Boolean debug;

    @PostMapping
    public void azureMessage(@RequestBody JSONObject json) {
        String content = json.getJSONObject("text").get("content").toString().replaceAll(" ", "");
        System.out.println(content);
        //获取用户userId
        String userId = json.getString("senderStaffId");
        String sessionWebhook = json.getString("sessionWebhook");
        DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);
        title(client, userId);
    }

    private void title(DingTalkClient client, String userId) {
        try {
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("markdown");
            OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
            markdown.setTitle("测试标题");
            markdown.setText(
                    " @" + userId + "  \n  " + "# 一级标题  \n  " + "## 二级标题  \n  " + "### 三级标题  \n  " + "#### 四级标题  \n  "
                    + "##### 五级标题  \n  " + "###### 六级标题  \n  ");
            request.setMarkdown(markdown);
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            System.out.println(userId);
            at.setAtUserIds(Arrays.asList(userId));
            //          isAtAll类型如果不为Boolean，请升级至最新SDK
            at.setIsAtAll(false);
            request.setAt(at);
            OapiRobotSendResponse response = client.execute(request);
            System.out.println(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void send(Set<String> emails, JSONObject jsonObject) {
        try {
            if (debug) {
                emails.add("chenhuizhong@hz-cpp.com");
            }
            messageSender.sendByEmail(emails, "PR消息通知",
                                      jsonObject.getJSONObject("detailedMessage").getString("markdown"));
        } catch (Exception e) {
            log.error("发生错误,参数:{}", jsonObject.toJSONString(), e);
        }
    }

    public static void main(String[] args) {
        System.out.println(Base64.getEncoder().encodeToString("hz-cpp-devops-robot".getBytes()));
    }
}
