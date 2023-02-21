package com.cpp.devops.controller;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2023-02-20 14:39
 **/

/**
 * 实现了机器人的简单问答功能
 */
@RestController
public class ChatController {

    @Value("${chat-gpt.secret}")
    String key;

    @RequestMapping(value = "/chat")
    public String helloRobots(@RequestBody(required = false) JSONObject json) {
        System.out.println(json);
        String content = json.getJSONObject("text").get("content").toString().replaceAll(" ", "");
        // 获取用户手机号，用于发送@消息
        // String mobile = getUserMobile(json.getString("senderStaffId"));
        String sessionWebhook = json.getString("sessionWebhook");
        DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);
        chat(client, content);
        return null;
    }

    /**
     * chatgpt3回答
     */
    public void chat(DingTalkClient client, String content) {
        try {
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            //1{"model": "text-davinci-003", "prompt": "Say this is a test", "temperature": 0, "max_tokens": 7}'
            Map<String, Object> param = new HashMap<>();
            //param.put("model", "text-davinci-003");
            param.put("prompt", content);
            param.put("temperature", 0.7);
            param.put("max_tokens", 1000);
            param.put("best_of", 1);
            param.put("top_p", 1);
            param.put("presence_penalty", 0);
            param.put("frequency_penalty", 0);
            text.setContent(get("https://api.openai.com/v1/engines/text-davinci-003-playground/completions", param));
            request.setText(text);
            OapiRobotSendResponse response = client.execute(request);
            System.out.println(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public String get(String url, Map<String, Object> params) {
        HttpRequest request = HttpRequest.get(url);
        request.contentType(ContentType.JSON.getValue());
        request.body(JSON.toJSONString(params));
        request.header("Authorization", key);
        HttpResponse response = request.execute();
        JSONObject jsonObject = JSON.parseObject(response.body());
        if (jsonObject.get("error") != null) {
            return jsonObject.getString("error");
        }
        JSONObject result = jsonObject.getJSONArray("choices").getJSONObject(0);
        return result.getString("text");
    }

}