package com.cpp.devops.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpp.devops.entity.Dept;
import com.cpp.devops.entity.User;
import com.cpp.devops.mapper.UserMapper;
import com.cpp.devops.service.DeptService;
import com.cpp.devops.service.UserService;
import com.cpp.devops.service.extend.DingTalkOpenApi;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse.DeptBaseResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-07 14:46:08
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private DingTalkOpenApi dingTalkOpenApi;

    @Autowired
    private DeptService deptService;

    @Override
    public List<String> emailToUserId(List<String> emailList) {
        List<String> userIds = new ArrayList<>();
        for (String email : emailList) {
            User user = this.getByEmail(email);
            if (user != null) {
                userIds.add(user.getUserid());
            }
        }
        return userIds;
    }

    @Override
    public void syncUsers() throws Exception {
        Set<Dept> deptSet = getDept(dingTalkOpenApi.getDeptList(1L));
        Set<User> userSet = new HashSet<>();

        for (Dept dept : deptSet) {
            for (String userId : dingTalkOpenApi.getUserIdList(dept.getDeptId())) {
                UserGetResponse response = dingTalkOpenApi.getUser(userId);
                User user = new User();
                BeanUtils.copyProperties(response, user);
                userSet.add(user);
            }
        }
        this.saveOrUpdateBatch(userSet);
        deptService.saveOrUpdateBatch(deptSet);

    }

    @Override
    public User getByEmail(String email) {
        return this.getOne(new QueryWrapper<User>().eq("email", email));
    }

    public Set<Dept> getDept(List<DeptBaseResponse> responseList) throws Exception {
        Set<Dept> deptSet = new HashSet<>();
        for (DeptBaseResponse response : responseList) {
            Dept dept = new Dept();
            BeanUtils.copyProperties(response, dept);
            deptSet.add(dept);
            List<DeptBaseResponse> subList = dingTalkOpenApi.getDeptList(dept.getDeptId());
            if (!subList.isEmpty()) {
                deptSet.addAll(getDept(subList));
            }
        }
        return deptSet;
    }
}

