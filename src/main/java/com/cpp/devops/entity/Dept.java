package com.cpp.devops.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * (Dept)表实体类
 *
 * @author makejava
 * @since 2022-09-07 13:45:13
 */
@SuppressWarnings("serial")
@Data
public class Dept extends Model<Dept> {

    @TableId
    private Long deptId;

    private String name;

    private Long parentId;

    private String creator;

    private Timestamp createTime;

    private String editor;

    private Timestamp editTime;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.deptId;
    }
}

