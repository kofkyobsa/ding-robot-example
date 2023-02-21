package com.cpp.devops.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-08 11:30
 **/
@Slf4j
public class AzureUtil {

    public static Set<String> getWorkItemEmail(JSONObject jsonObject) {
        try {
            String assigned = jsonObject.getJSONObject("resource").getJSONObject("revision").getJSONObject(
                    "fields").getString("System.AssignedTo");
            String created = jsonObject.getJSONObject("resource").getJSONObject("revision").getJSONObject(
                    "fields").getString("System.CreatedBy");
            String revised = jsonObject.getJSONObject("resource").getJSONObject("revisedBy").getString("uniqueName");
            List<String> list = new ArrayList<>();
            list.add(assigned);
            list.add(created);
            Set<String> result = list.stream().map(t -> {
                String[] ss = t.split("<");
                String e = ss[ss.length - 1];
                return e.substring(0, e.length() - 1);
            }).collect(Collectors.toSet());
            result.remove(revised);
            return result;
        } catch (Exception e) {
            log.error("解析失败:{}", jsonObject.toJSONString(), e);
            return Collections.emptySet();
        }
    }

    public static Set<String> getPullRequestCreatedEmail(JSONObject jsonObject) {
        try {
            String createdBy = jsonObject.getJSONObject("resource").getJSONObject("createdBy").getString("uniqueName");
            List<String> reviewers = jsonObject.getJSONObject("resource").getJSONArray("reviewers").stream().map(
                    t -> ((LinkedHashMap) t).get("uniqueName").toString()).collect(Collectors.toList());
            Set<String> result = new HashSet<>();
            result.addAll(reviewers);
            result.add(createdBy);
            return result;
        } catch (Exception e) {
            log.error("解析失败:{}", jsonObject.toJSONString(), e);
            return Collections.emptySet();
        }
    }

    public static Set<String> getPullRequestCommentEmail(JSONObject jsonObject) {
        try {
            String author = jsonObject.getJSONObject("resource").getJSONObject("comment").getJSONObject(
                    "author").getString("uniqueName");
            List<String> reviewers = jsonObject.getJSONObject("resource").getJSONObject("pullRequest").getJSONArray(
                    "reviewers").stream().map(t -> ((LinkedHashMap) t).get("uniqueName").toString()).collect(
                    Collectors.toList());
            String createdBy = jsonObject.getJSONObject("resource").getJSONObject("pullRequest").getJSONObject(
                    "createdBy").getString("uniqueName");

            Set<String> result = new HashSet<>();
            result.addAll(reviewers);
            result.add(createdBy);
            result.remove(author);
            return result;
        } catch (Exception e) {
            log.error("解析失败:{}", jsonObject.toJSONString(), e);
            return Collections.emptySet();
        }
    }

    public static Set<String> getBuildEmail(JSONObject jsonObject) {
        try {
            String lastChangedBy = jsonObject.getJSONObject("resource").getJSONObject("lastChangedBy").getString(
                    "uniqueName");
            List<String> reviewers = jsonObject.getJSONObject("resource").getJSONArray("requests").stream().map(
                    t -> (JSON.parseObject(JSON.toJSONString(t)).getJSONObject("requestedFor").get(
                            "uniqueName").toString())).collect(Collectors.toList());
            Set<String> result = new HashSet<>();
            result.addAll(reviewers);
            result.add(lastChangedBy);
            return result;
        } catch (Exception e) {
            log.error("解析失败:{}", jsonObject.toJSONString(), e);
            return Collections.emptySet();
        }
    }

    public static Set<String> getReleaseEmail(JSONObject jsonObject) {
        try {
            String lastChangedBy = jsonObject.getJSONObject("resource").getJSONObject("lastChangedBy").getString(
                    "uniqueName");
            List<String> reviewers = jsonObject.getJSONObject("resource").getJSONArray("requests").stream().map(
                    t -> (JSON.parseObject(JSON.toJSONString(t)).getJSONObject("requestedFor").get(
                            "uniqueName").toString())).collect(Collectors.toList());
            Set<String> result = new HashSet<>();
            result.addAll(reviewers);
            result.add(lastChangedBy);
            return result;
        } catch (Exception e) {
            log.error("解析失败:{}", jsonObject.toJSONString(), e);
            return Collections.emptySet();
        }
    }
}
