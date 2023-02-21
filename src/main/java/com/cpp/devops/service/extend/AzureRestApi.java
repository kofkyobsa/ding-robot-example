package com.cpp.devops.service.extend;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ding-message-service
 * @description: Azure rest api
 * @author: HuiZhong
 * @create: 2022-09-08 09:47
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "azure", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class AzureRestApi {

    private String token;
    private String company;

    public String getProjectName(String projectId) {
        String rsp = HttpRequest.get(
                String.format("https://dev.azure.com/%s/_apis/projects/%s?api-version=7.1-preview.4", company,
                              projectId)).basicAuth("", token).execute().body();
        return JSON.parseObject(rsp).getString("name");
    }

    public String getAssignedUser(String projectId, String workItemId) {
        String rsp = HttpRequest.get(
                String.format("https://dev.azure.com/henhen/%s/_apis/wit/workitems/%s?api-version=6.0", projectId,
                              workItemId)).basicAuth("", token).execute().body();
        return JSON.parseObject(rsp).getJSONObject("fields").getJSONObject("System.AssignedTo").getString("uniqueName");
    }

    public List<String> getTeams(String projectId) {
        String rsp = HttpRequest.get(
                String.format("https://dev.azure.com/%s/_apis/projects/%s/teams", company, projectId)).basicAuth("",
                                                                                                                 token).execute().body();
        return JSON.parseObject(rsp).getJSONArray("value").stream().map(t -> ((JSONObject) t).getString("id")).collect(
                Collectors.toList());
    }

    public List<String> getEmails(String teamId) {
        String rsp = HttpRequest.get(
                String.format("https://dev.azure.com/%s/_apis/projects/devops/teams/%s/members?api-version=6.0",
                              company, teamId)).basicAuth("", token).execute().body();
        return JSON.parseObject(rsp).getJSONArray("value").stream().map(
                t -> ((JSONObject) t).getString("uniqueName")).collect(Collectors.toList());

    }

}
