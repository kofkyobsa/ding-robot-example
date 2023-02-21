package com.cpp.devops.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (Conversation)表实体类
 *
 * @author makejava
 * @since 2022-09-09 13:44:12
 */
@SuppressWarnings("serial")
@Data
public class Conversation extends Model<Conversation> {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String openConversationId;

    private String chatid;

    private String name;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

