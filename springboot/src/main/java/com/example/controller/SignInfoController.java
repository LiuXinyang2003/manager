package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.entity.*;
import com.example.entity.SignInfo;
import com.example.service.SignInfoService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 签到信息表操作接口
 **/
@RestController
@RequestMapping("/signInfo")
public class SignInfoController {

    @Resource
    private SignInfoService signInfoService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody SignInfo signInfo) {
        if (signInfo.getId() == null) {
            signInfo.setStart(DateUtil.date().toString());
            signInfo.setOvertime(DateUtil.offset(DateUtil.date(),DateField.MINUTE,20).toString());
        }
        signInfoService.add(signInfo);
        return Result.success();
    }

    @PostMapping("/signInfoAll")
    public Result sigIn(@RequestBody SignInfo signInfo) {
        signInfoService.signAdd(signInfo);
        return Result.success();
    }



    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        signInfoService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        signInfoService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/updateClassId")
    public Result updateClassId(@RequestBody SignInfo signInfo) {
        signInfoService.updateClassId(signInfo);
        return Result.success();
    }

    @PutMapping("/updateState")
    public Result updateState() {
        signInfoService.updateState();
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody SignInfo signInfo) {
        signInfoService.updateById(signInfo);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        SignInfo signInfo = signInfoService.selectById(id);
        return Result.success(signInfo);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<SignInfo> list = signInfoService.selectAll();
        return Result.success(list);
    }

//    /**
//     * 分页查询
//     */
//    @GetMapping("/selectPage")
//    public Result selectPage( SignInfo signInfo,
//                             @RequestParam(defaultValue = "1") Integer pageNum,
//                             @RequestParam(defaultValue = "10") Integer pageSize) {
//        System.out.println("SignInfo    "+signInfo);
//        System.out.println("name   "+signInfo.getName());
//        PageInfo<SignInfo> page = signInfoService.selectPage(signInfo, pageNum, pageSize);
//        return Result.success(page);
//    }
    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Params params) {

        PageInfo<SignInfo> page = signInfoService.selectPage(params);
//        System.out.println("页面时"+page);
        return Result.success(page);
    }

}