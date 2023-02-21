package com.cpp.devops.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cpp.devops.entity.Conversation;
import com.cpp.devops.service.ConversationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Conversation)表控制层
 *
 * @author makejava
 * @since 2022-09-09 13:44:12
 */
@RestController
@RequestMapping("conversation")
public class ConversationController extends ApiController {

    /**
     * 服务对象
     */
    @Resource
    private ConversationService conversationService;

    /**
     * 分页查询所有数据
     *
     * @param page         分页对象
     * @param conversation 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R<Page<Conversation>> selectAll(Page<Conversation> page, Conversation conversation) {
        return success(this.conversationService.page(page, new QueryWrapper<>(conversation)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R<Conversation> selectOne(@PathVariable Serializable id) {
        return success(this.conversationService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param conversation 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Conversation conversation) {
        return success(this.conversationService.save(conversation));
    }

    /**
     * 修改数据
     *
     * @param conversation 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Conversation conversation) {
        return success(this.conversationService.updateById(conversation));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.conversationService.removeByIds(idList));
    }
}

