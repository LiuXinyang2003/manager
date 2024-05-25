package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.*;
import com.example.entity.SignInfo;
import com.example.service.SignInfoService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签到信息表操作接口
 **/
@RestController
@RequestMapping("/signInfo")
public class SignInfoController {

    @Resource
    private SignInfoService signInfoService;



    @GetMapping("/export")
    public Result export(HttpServletResponse response, @RequestParam(value = "role",required = true) String role,
                         @RequestParam(value = "classId",required = false,defaultValue = "0")  String classIdStr,
                         @RequestParam(value = "name",required = true) String name,
                         @RequestParam(value = "name2",required = true) String name2,
                         @RequestParam(value = "classNameP",required = true) String classNameP,
                         @RequestParam(value = "username",required = true) String username
                         ) throws IOException {
        Integer classId;
        try {
            // 尝试将classIdStr转换为Integer
            classId = Integer.parseInt(classIdStr);
        } catch (NumberFormatException e) {
            // 如果转换失败（例如，当classIdStr是"null"时），则使用默认值0
            classId = 0;
        }
        System.out.println("role的con值"+role);
        System.out.println("classId的con值"+classId);
        List<SignInfo> both = signInfoService.findAll(role,classId,name,name2,classNameP,username);
        List<Map<String, Object>> list = new ArrayList<>(both.size());
        for (SignInfo signInfo : both) {
            Map<String, Object> row = new HashMap<>();
            row.put("签到主题",signInfo.getName());
            row.put("学生名",signInfo.getStudentName());
            row.put("签到时间",signInfo.getStart());
            row.put("截止时间",signInfo.getOvertime());
            row.put("班级",signInfo.getClassName());
            row.put("签到状态",signInfo.getState());
            list.add(row);
        }
        ExcelWriter wr = ExcelUtil.getWriter(true);
        wr.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=signinfo.xlsx");
        ServletOutputStream out = response.getOutputStream();
        wr.flush(out, true);
        wr.close();
        IoUtil.close(System.out);
        return Result.success();

    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody SignInfo signInfo) {
        if (signInfo.getId() == null) {
            signInfo.setStart(DateUtil.date().toString());
            signInfo.setOvertime(DateUtil.offset(DateUtil.date(),DateField.MINUTE,20).toString());
            signInfo.setUsername(TokenUtils.getCurrentUser().getUsername());
            signInfo.setRole(TokenUtils.getCurrentUser().getRole());
        }
        System.out.println("江西"+signInfo.getRole());
        signInfoService.add(signInfo);
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