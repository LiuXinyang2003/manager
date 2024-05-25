package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Classes;
import com.example.entity.Params;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.service.StudentService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员前端操作接口
 **/
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;





    @GetMapping("/start")
    public String star(){
        /*
         * controller里的一个方法，他其实就是我们平常说的web项目的一个接口的密码
         * 可以马上在这个方法再加上一个Url
         * 也可以指定请求方式：GET，POST，PUT，DELETE
         * @return
         * */
        System.out.println("灭好的");
        return "欢迎，这是我的第一个springboot的项目";
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Student student) {

        studentService.add(student);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        studentService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        studentService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Student student) {
        studentService.updateById(student);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Student student = studentService.selectById(id);
        return Result.success(student);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Student student ) {
        List<Student> list = studentService.selectAll(student);
        return Result.success(list);
    }
    @GetMapping("/getTotal")
    public Integer getTotal(Student student ) {
        List<Student> list = studentService.selectAll(student);
        System.out.println(list.size());
        return list.size();
    }
//    @GetMapping("/getData")
//    public Map<Integer,Integer> getData(Student student) {
//        for(Map.Entry<Integer,Integer> entry: studentService.getData(student).entrySet()) {
//            System.out.println("键值对为"+entry.getKey()+ " " + entry.getValue());
//        }
//        return studentService.getData(student);
//    }
    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Params params) {
        PageInfo<Student> page = studentService.selectPage(params);
        return Result.success(page);
    }

    @GetMapping("/selectPage2")
    public Result selectPage2(Params params) {
        PageInfo<Student> page = studentService.selectPage2(params);
        return Result.success(page);
    }


    @GetMapping("/selectAdd")
    public Result selectAdd(@RequestParam String name) {
        Student student = studentService.selectAdd(name);
        return Result.success(student);
    }


    @GetMapping("/export")
    public Result export(HttpServletResponse response,@RequestParam(value = "role",required = true) String role,
                         @RequestParam(value = "classId",required = false,defaultValue = "0")  String classIdStr,
//                         @RequestParam(value = "username",required = true) String username,
                         @RequestParam(value = "name",required = true) String name,
                         @RequestParam(value = "classNameP",required = true) String classNameP) throws IOException {
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
        List<Student> both = studentService.findAll(role,classId,name,classNameP);
//        if(CollectionUtil.isEmpty(both)){
//            throw new CustomException("未找到数据");
//        }
        List<Map<String, Object>> list = new ArrayList<>(both.size());
        for (Student student : both) {
            Map<String, Object> row = new HashMap<>();
            row.put("姓名",student.getName());
            row.put("性别",student.getGender());
            row.put("学院",student.getCollegeName());
            row.put("专业",student.getSpecialityName());
            row.put("班级",student.getClassName());
            list.add(row);
        }
        ExcelWriter wr = ExcelUtil.getWriter(true);
        wr.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=student.xlsx");
        ServletOutputStream out = response.getOutputStream();
        wr.flush(out, true);
        wr.close();
        IoUtil.close(System.out);
        return Result.success();

    }

    @GetMapping("/exportPer")
    public Result exportPer(HttpServletResponse response,@RequestParam(value = "role",required = true) String role,
                         @RequestParam(value = "classId",required = false,defaultValue = "0")  String classIdStr,
//                       @RequestParam(value = "username",required = true) String username,
                         @RequestParam(value = "name",required = true) String name) throws IOException {
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
        List<Student> both = studentService.findAllPer(role,classId,name);
//        if(CollectionUtil.isEmpty(both)){
//            throw new CustomException("未找到数据");
//        }
        List<Map<String, Object>> list = new ArrayList<>(both.size());
        for (Student student : both) {
            Map<String, Object> row = new HashMap<>();

            row.put("姓名",student.getName());
            row.put("性别",student.getGender());
            row.put("学院",student.getCollegeName());
            row.put("专业",student.getSpecialityName());
            row.put("班级",student.getClassName());
            list.add(row);
        }
        ExcelWriter wr = ExcelUtil.getWriter(true);
        wr.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=student.xlsx");
        ServletOutputStream out = response.getOutputStream();
        wr.flush(out, true);
        wr.close();
        IoUtil.close(System.out);
        return Result.success();

    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        List<Student> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(Student.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            for (Student student : infoList) {
                System.out.println("学生名字是"+student.getName());
                System.out.println("学生性别是"+student.getGender());
                System.out.println("学生账号是"+student.getUsername());
                System.out.println("学生班级是"+student.getClassName());
                try {
                    studentService.add2(student);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }



}