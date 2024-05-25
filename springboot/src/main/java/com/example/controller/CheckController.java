package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import com.example.common.Result;
import com.example.entity.*;
import com.example.service.CheckService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 签到信息表前端操作接口
 **/
@RestController
@RequestMapping("/check")
public class CheckController {

    @Resource
    private CheckService checkService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Check check) {
        if (check.getId() == null) {
            check.setUsername(TokenUtils.getCurrentUser().getUsername());
            check.setRole(TokenUtils.getCurrentUser().getRole());
        }
        checkService.add(check);
        return Result.success();
    }
    @PostMapping("/signIn")
    //测试sign2
    public Result sigIn() {

//        checkService.signAdd(check);
        return Result.success();
    }

    @PostMapping("/signIn2")
    public Result sigIn2(@RequestBody SignInData signInData) {
        signInData.getCheck().setUsername(TokenUtils.getCurrentUser().getUsername());
        signInData.getCheck().setRole(TokenUtils.getCurrentUser().getRole());
        System.out.println("班级id是"+signInData.getCheck().getClassId());
        checkService.signAdd2(signInData);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        checkService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        checkService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Check check) {
        check.setStart(DateUtil.date().toString());
        check.setOvertime(DateUtil.offset(DateUtil.date(),DateField.MINUTE,20).toString());
        checkService.updateById(check);
        Check newRow = check;
        return Result.success(newRow);
    }


    @PutMapping("/updateClassId")
    public Result updateClassId() {
        checkService.updateClassId();
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Check check = checkService.selectById(id);
        return Result.success(check);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Check> list = checkService.selectAll();
        return Result.success(list);
    }
    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage( Params params) {
        PageInfo<Check> page = checkService.selectPage(params);
        return Result.success(page);
    }

}