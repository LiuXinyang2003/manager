package com.example.controller;

import com.example.common.Result;
import com.example.entity.College;
import com.example.entity.Params;
import com.example.entity.Speciality;
import com.example.service.CollegeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 学院信息表前端操作接口
 **/
@RestController
@RequestMapping("/college")
public class CollegeController {

    @Resource
    private CollegeService collegeService;


    @GetMapping("/{collegeId}/specialties")
    public Result getSpecialtiesByCollegeId(@PathVariable Integer collegeId) {
        List<Speciality> specialties = collegeService.getSpecialtiesByCollegeId(collegeId);
        return Result.success(specialties);
    }
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody College college) {
        collegeService.add(college);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        collegeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        collegeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody College college) {
        collegeService.updateById(college);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        College college = collegeService.selectById(id);
        return Result.success(college);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(College college ) {
        List<College> list = collegeService.selectAll(college);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Params params) {
        PageInfo<College> page = collegeService.selectPage(params);
        return Result.success(page);
    }

    @GetMapping("/search")
    public Result findBySearch(Params params){
        System.out.println("名字为"+params.getName());
        PageInfo<College> info = collegeService.findBySearch(params);
        return Result.success(info);
    }

}