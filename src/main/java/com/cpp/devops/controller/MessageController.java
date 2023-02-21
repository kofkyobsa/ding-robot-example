package com.cpp.devops.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cpp.devops.entity.CommonMessage;
import com.cpp.devops.service.DingMessageSender;
import com.cpp.devops.system.Limit;
import com.cpp.devops.util.AzureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @program: ding-message-service
 * @description: Azure webhook回调
 * @author: HuiZhong
 * @create: 2022-09-07 16:25
 **/
@Slf4j
@RestController
@RequestMapping("message")
@Limit(limitNum = 24, name = "ding")
public class MessageController {

    @Autowired
    private DingMessageSender messageSender;

    @Value("${ding.debug:false}")
    private Boolean debug;

    @PostMapping("workItem")
    public void azureMessage(@RequestBody JSONObject jsonObject) {
        Set<String> emails = AzureUtil.getWorkItemEmail(jsonObject);
        send(emails, "工单", jsonObject);
    }

    @PostMapping("build")
    public void build(@RequestBody JSONObject jsonObject) {
        Set<String> emails = AzureUtil.getBuildEmail(jsonObject);
        send(emails, "构建", jsonObject);
    }

    @PostMapping("pullRequest")
    public void pullRequestCreated(@RequestBody JSONObject jsonObject) {
        Set<String> emails = AzureUtil.getPullRequestCreatedEmail(jsonObject);
        send(emails, "PR", jsonObject);
    }

    @PostMapping("pullRequestComment")
    public void pullRequestComment(@RequestBody JSONObject jsonObject) {
        Set<String> emails = AzureUtil.getPullRequestCommentEmail(jsonObject);
        send(emails, "PR评论", jsonObject);
    }

    @PostMapping("release")
    public void releaseSend(@RequestBody JSONObject jsonObject) {
        try {
            String projectId = jsonObject.getJSONObject("resourceContainers").getJSONObject("project").getString("id");

            messageSender.sendGroupMsg(projectId, "发布消息通知",
                                       jsonObject.getJSONObject("detailedMessage").getString("markdown"));

        } catch (Exception e) {
            log.error("发生错误,参数:{}", jsonObject.toJSONString(), e);
        }
    }

    private void send(Set<String> emails, String title, JSONObject jsonObject) {
        try {
            if (debug) {
                emails.add("chenhuizhong@hz-cpp.com");
            }
            if (emails.isEmpty()) {
                return;
            }
            messageSender.sendByEmail(emails, title + "消息通知",
                                      jsonObject.getJSONObject("detailedMessage").getString("markdown"));
        } catch (Exception e) {
            log.error("发生错误,参数:{}", jsonObject.toJSONString(), e);
        }
    }

    @PostMapping("send")
    public void sendCommonMessage(@RequestBody CommonMessage commonMessage) {
        try {
            if (debug) {
                commonMessage.getEmails().add("chenhuizhong@hz-cpp.com");
            }
            if (commonMessage.getEmails().isEmpty()) {
                return;
            }
            messageSender.sendByEmail(commonMessage.getEmails(), commonMessage.getTitle(), commonMessage.getMessage());
        } catch (Exception e) {
            log.error("发生错误,参数:{}", JSON.toJSONString(commonMessage), e);
        }
    }

    //public static void main(String[] args) {
    //    CommonMessage msg = new CommonMessage();
    //    msg.setMessage("test context");
    //    msg.setTitle("test title");
    //    msg.setEmails(new HashSet<>());
    //    System.out.println(JSON.toJSONString(msg));
    //}

}
