package com.cpp.devops.service.extend;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.TimedCache;
import com.aliyun.dingtalkrobot_1_0.Client;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOHeaders;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTORequest;
import com.aliyun.dingtalkrobot_1_0.models.OrgGroupSendHeaders;
import com.aliyun.dingtalkrobot_1_0.models.OrgGroupSendRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatCreateRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserListidRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiChatCreateResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserListidResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse.DeptBaseResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-07 09:37
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "ding", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class DingTalkOpenApi {

    private static final String CACHE_KEY = "AccessToken";

    private String                appKey;
    private String                appSecret;
    private String                manager;
    private Cache<String, String> cache;

    public List<DeptBaseResponse> getDeptList(Long deptId) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
        req.setDeptId(deptId);
        req.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse rsp = client.execute(req, getAccessToken());
        return rsp.getResult();
    }

    public List<String> getUserIdList(Long deptId) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listid");
        OapiUserListidRequest req = new OapiUserListidRequest();
        req.setDeptId(deptId);
        OapiUserListidResponse rsp = client.execute(req, getAccessToken());
        return rsp.getResult().getUseridList();
    }

    public OapiChatCreateResponse createChat(String name, List<String> userIds) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/create");
        OapiChatCreateRequest req = new OapiChatCreateRequest();
        req.setName(name);
        req.setOwner(manager);
        req.setUseridlist(userIds);
        return client.execute(req, getAccessToken());
    }

    public void sendGroupMessage(String openConversationId, String msgKey, String msg) throws Exception {
        Client client = createClient();
        OrgGroupSendHeaders orgGroupSendHeaders = new OrgGroupSendHeaders();
        orgGroupSendHeaders.xAcsDingtalkAccessToken = getAccessToken();
        OrgGroupSendRequest orgGroupSendRequest = new OrgGroupSendRequest().setMsgParam(msg).setMsgKey(
                msgKey).setOpenConversationId(openConversationId).setRobotCode(appKey);
        try {
            client.orgGroupSendWithOptions(orgGroupSendRequest, orgGroupSendHeaders, new RuntimeOptions());
        } catch (TeaException err) {
            log.error("发送信息失败,code:{},msg:{}", err.code, err.message);

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            log.error("发送信息失败,code:{},msg:{}", err.code, err.message);
            throw err;
        }
    }

    public void notifyMsg(List<String> userIds, String msgKey, String msgParam) throws Exception {
        Client client = createClient();
        BatchSendOTOHeaders batchSendOTOHeaders = new BatchSendOTOHeaders();
        batchSendOTOHeaders.xAcsDingtalkAccessToken = getAccessToken();
        BatchSendOTORequest batchSendOTORequest = new BatchSendOTORequest().setRobotCode(appKey).setUserIds(
                userIds).setMsgKey(msgKey).setMsgParam(msgParam);
        try {
            client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
        } catch (TeaException err) {
            log.error("发送信息失败,code:{},msg:{}", err.code, err.message);

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            log.error("发送信息失败,code:{},msg:{}", err.code, err.message);
            throw err;
        }
    }

    public static Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    public UserGetResponse getUser(String userId) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage("zh_CN");
        OapiV2UserGetResponse rsp = client.execute(req, getAccessToken());
        return rsp.getResult();
    }

    public String getAccessToken() throws Exception {
        if (cache != null && cache.containsKey(CACHE_KEY)) {
            return cache.get(CACHE_KEY);
        }
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        if (response.getErrcode().equals(0L)) {
            cache = new TimedCache<>(response.getExpiresIn());
            cache.put(CACHE_KEY, response.getAccessToken());
            return response.getAccessToken();
        }
        throw new RuntimeException(
                "getAccessToken异常,code:" + response.getErrcode() + ",message:" + response.getErrmsg());
    }
}
