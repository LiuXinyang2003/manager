package com.example.controller;

import com.example.common.Result;
import com.example.entity.Params;
import com.example.entity.Teacher1;
import com.example.service.Teacher1Service;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任课老师前端操作接口
 **/
@RestController
@RequestMapping("/teacher1")
public class Teacher1Controller {

    @Resource
    private Teacher1Service teacher1Service;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Teacher1 teacher1) {
        teacher1Service.add(teacher1);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        teacher1Service.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        teacher1Service.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Teacher1 teacher1) {
        teacher1Service.updateById(teacher1);
        return Result.success();
    }


    @PutMapping("/updateClassId")
    public Result updateClassId(@RequestBody Teacher1 teacher1) {
        teacher1Service.updateClassId(teacher1);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Teacher1 teacher1 = teacher1Service.selectById(id);
        return Result.success(teacher1);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Teacher1 teacher1 ) {
        List<Teacher1> list = teacher1Service.selectAll(teacher1);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Params params) {
        PageInfo<Teacher1> page = teacher1Service.selectPage(params);
        return Result.success(page);
    }

}