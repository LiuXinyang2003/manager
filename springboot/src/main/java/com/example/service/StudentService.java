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


    public List<Student> findAll() {

        return studentMapper.findAll();
    }
    /**
     * 分页查询
     */
    public PageInfo<Student> selectPage(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        String current = params.getRole();
        System.out.println("大师傅士大夫"+params.getRole());
        if(RoleEnum.TEACHER.toString().equals(current)){
            System.out.println("的肌肤水分及");
            List<Student> list = studentMapper.selectTeacher(params);
            return PageInfo.of(list);
        }
        else{
            List<Student> list = studentMapper.selectAll(params);
            return PageInfo.of(list);
        }
    }

    public Student selectAdd(String name) {
        System.out.println("名字2是"+name);
        Student student = studentMapper.selectAdd(name);
        System.out.println("飞机的设计覅2"+student);
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
        System.out.println("登录信息的学生"+dbStudent.getToken());
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