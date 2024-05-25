package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.CheckMapper;
import com.example.mapper.StudentMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生业务处理
 **/
@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private CheckMapper checkMapper;
    /**
     * 新增
     */
    public void add(Student student) {
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());
        if (ObjectUtil.isNotNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(student.getPassword())) {
            student.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(student.getName())) {
            student.setName(student.getUsername());
        }
        student.setRole(RoleEnum.STUDENT.name());
        studentMapper.insert(student);
    }

    public void add2(Student student) {
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());

        System.out.println("用户名是"+student.getUsername());
        if (ObjectUtil.isNotNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(student.getPassword())) {
            student.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(student.getName())) {
            student.setName(student.getUsername());
        }
        student.setUsername(student.getUsername());
        student.setRole(RoleEnum.STUDENT.name());
        student.setCollegeId(studentMapper.selectCollegeId(student));;
        student.setSpecialityId(studentMapper.selectSpecialityId(student));
        student.setClassId(studentMapper.selectClassId(student));
        studentMapper.insert(student);
    }


    /**
     * 删除
     */
    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            studentMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Student student) {
        studentMapper.updateById(student);
    }

    /**
     * 根据ID查询
     */
    public Student selectById(Integer id) {
        return studentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Student> selectAll(Student student) {

        return studentMapper.selectStudent(student);
    }


    public List<Student> findAll(String role,Integer classId,String name,String classNameP) {
        System.out.println("role的值"+role);
        if(RoleEnum.TEACHER.toString().equals(role)){
            System.out.println("进入Service层"+role);
            List<Student> list = studentMapper.selectTeacher2(classId,name);
            return list;
        }
        else{
            List<Student> list = studentMapper.selectAll2(name,classNameP);
            return list;
        }
    }

    public List<Student> findAllPer(String role,Integer classId,String name) {
        System.out.println("role的值"+role);
            System.out.println("进入Service层"+role);
            List<Student> list = studentMapper.selectTeacher2(classId,name);
            return list;
    }
    /**
     * 分页查询
     */
    public PageInfo<Student> selectPage(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        String current = params.getRole();
        if(RoleEnum.TEACHER.toString().equals(current)){
            List<Student> list = studentMapper.selectTeacher(params);
            return PageInfo.of(list);
        }
        else{
            List<Student> list = studentMapper.selectAll(params);
            return PageInfo.of(list);
        }
    }
    public PageInfo<Student> selectPage2(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        List<Student> list = studentMapper.selectTeacher(params);
        return PageInfo.of(list);
    }

    public Student selectAdd(String name) {
        Student student = studentMapper.selectAdd(name);
        return student;
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbStudent.getId() + "-" + RoleEnum.STUDENT.name();
        String token = TokenUtils.createToken(tokenData, dbStudent.getPassword());
        dbStudent.setToken(token);
        return dbStudent;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        Student student = new Student();
        BeanUtils.copyProperties(account, student);
        add(student);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbStudent.setPassword(account.getNewPassword());
        studentMapper.updateById(dbStudent);
    }

}