package com.example.controller;

import com.example.common.Result;
import com.example.entity.Classes;
import com.example.entity.College;
import com.example.entity.Params;
import com.example.entity.Speciality;
import com.example.service.SpecialityService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 专业信息表前端操作接口
 **/
@RestController
@RequestMapping("/speciality")
public class SpecialityController {

    @Resource
    private SpecialityService specialityService;




    @GetMapping("/{specialityId}/classes")
    public Result getClassesBySpecialityId(@PathVariable Integer specialityId) {
        List<Classes> classes = specialityService.getClassesBySpecialityId(specialityId);
        return Result.success(classes);
    }
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Speciality speciality) {
        specialityService.add(speciality);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        specialityService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        specialityService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Speciality speciality) {
        specialityService.updateById(speciality);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Speciality speciality = specialityService.selectById(id);
        return Result.success(speciality);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Speciality speciality ) {
        List<Speciality> list = specialityService.selectAll(speciality);
        return Result.success(list);
    }

    @GetMapping("/getMajorsByCollege")
    public Result getMajorsByCollege(Speciality speciality) {
        List<Speciality> list = specialityService.getMajorsByCollege(speciality);
        return Result.success(list);
    }
    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Params params) {
        PageInfo<Speciality> page = specialityService.selectPage(params);
        return Result.success(page);
    }

}