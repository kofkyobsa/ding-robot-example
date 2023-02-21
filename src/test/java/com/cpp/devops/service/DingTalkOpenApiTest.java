package com.cpp.devops.service;

import com.cpp.devops.service.extend.DingTalkOpenApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DingTalkOpenApiTest {

    @Autowired
    private DingTalkOpenApi client;

    @Test
    public void getAccessToken() throws Exception {
        System.out.println(client.getAccessToken());
    }

    @Test
    public void getDeptList() throws Exception {
        client.getDeptList(1L);
    }

    @Test
    public void notifyMsg() throws Exception {
        client.notifyMsg(Arrays.asList(new String[] { "16611335281629788" }), "sampleMarkdown",
                         "{\"text\": \"hello text\",\"title\": \"hello title\"}");
    }
}