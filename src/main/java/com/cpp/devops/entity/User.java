package com.cpp.devops.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2022-09-07 14:46:08
 */
@SuppressWarnings("serial")
@Data
public class User extends Model<User> {

    private String email;

    @TableId
    private String userid;

    private String jobNumber;

    private String unionid;

    private String managerUserid;

    private String mobile;

    private String name;

    private String title;

    private Timestamp hiredDate;

    private String workplace;

    private String orgEmail;

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
        return this.userid;
    }
}

