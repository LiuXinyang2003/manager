package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Params;
import com.example.entity.Teacher1;
import com.example.exception.CustomException;
import com.example.mapper.Teacher1Mapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任课老师业务处理
 **/
@Service
public class Teacher1Service {

    @Resource
    private Teacher1Mapper teacher1Mapper;

    /**
     * 新增
     */
    public void add(Teacher1 teacher1) {
        Teacher1 dbTeacher1 = teacher1Mapper.selectByUsername(teacher1.getUsername());
        if (ObjectUtil.isNotNull(dbTeacher1)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(teacher1.getPassword())) {
            teacher1.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(teacher1.getName())) {
            teacher1.setName(teacher1.getUsername());
        }
        teacher1.setRole(RoleEnum.TEACHER1.name());
        teacher1Mapper.insert(teacher1);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        teacher1Mapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            teacher1Mapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Teacher1 teacher1) {
        teacher1Mapper.updateById(teacher1);
    }


    public void updateClassId(Teacher1 teacher1) {
        teacher1Mapper.updateClassId(teacher1);
    }

    /**
     * 根据ID查询
     */
    public Teacher1 selectById(Integer id) {
        return teacher1Mapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Teacher1> selectAll(Teacher1 teacher1) {
        return teacher1Mapper.selectTeacher(teacher1);
    }

    /**
     * 分页查询
     */
    public PageInfo<Teacher1> selectPage(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        List<Teacher1> list = teacher1Mapper.selectAll(params);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbTeacher1 = teacher1Mapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbTeacher1)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbTeacher1.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbTeacher1.getId() + "-" + RoleEnum.TEACHER1.name();
        String token = TokenUtils.createToken(tokenData, dbTeacher1.getPassword());
        dbTeacher1.setToken(token);
        return dbTeacher1;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        Teacher1 teacher1 = new Teacher1();
        BeanUtils.copyProperties(account, teacher1);
        add(teacher1);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Teacher1 dbTeacher1 = teacher1Mapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbTeacher1)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbTeacher1.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbTeacher1.setPassword(account.getNewPassword());
        teacher1Mapper.updateById(dbTeacher1);
    }

}