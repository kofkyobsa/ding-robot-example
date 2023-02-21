package com.cpp.devops.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpp.devops.entity.Dept;
import com.cpp.devops.mapper.DeptMapper;
import com.cpp.devops.service.DeptService;
import org.springframework.stereotype.Service;

/**
 * (Dept)表服务实现类
 *
 * @author makejava
 * @since 2022-09-07 13:45:13
 */
@Service("deptService")
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}

