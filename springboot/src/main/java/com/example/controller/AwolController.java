package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Awol;
import com.example.entity.Params;
import com.example.entity.Teacher;
import com.example.service.AwolService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 请假信息表前端操作接口
 **/
@RestController
@RequestMapping("/awol")
public class AwolController {

    @Resource
    private AwolService awolService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Awol awol) {
        if (awol.getId() == null) {
            awol.setTime(DateUtil.today());
            awol.setName(TokenUtils.getCurrentUser().getName());
        }
        System.out.println("我是觉得司机"+awol.getClassId()+TokenUtils.getCurrentUser().getClassId());
        awolService.add(awol);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        awolService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        awolService.deleteBatch(ids);
        return Result.success();
    }

    @Transactional
    @PutMapping("/updateClassId")
    public Result updateClassId() {
        awolService.updateClassId();
        return Result.success();
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Awol awol) {
        awolService.updateById(awol);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Awol awol = awolService.selectById(id);
        return Result.success(awol);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Awol> list = awolService.selectAll();
        return Result.success(list);
    }
    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Params params) {
        PageInfo<Awol> page = awolService.selectPage(params);
        return Result.success(page);
    }

}