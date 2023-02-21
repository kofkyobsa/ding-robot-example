package com.cpp.devops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cpp.devops.entity.Conversation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Conversation)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-09 13:44:12
 */
public interface ConversationMapper extends BaseMapper<Conversation> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Conversation> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Conversation> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Conversation> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Conversation> entities);

}

