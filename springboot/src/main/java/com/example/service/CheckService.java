package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.CheckMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 签到信息表业务处理
 **/
@Service
public class CheckService {

    @Resource
    private CheckMapper checkMapper;

    /**
     * 新增
     */
    public void add(Check check) {
        System.out.println("小帕才2"+check.getRole());
        checkMapper.insert(check);
    }

    public void signAdd(Check check) {
        checkMapper.signAdd(check);
    }

    public void signAdd2(SignInData signInData) {
        for (int i=0;i<signInData.getStudents().size();i++){
            if (signInData.getStudents().get(i).getClassName().equals(signInData.getCheck().getClassName())){
                checkMapper.signAdd2(signInData.getCheck(),signInData.getStudents().get(i).getName());
            }
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        checkMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            checkMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Check check) {
        checkMapper.updateById(check);
    }

    /**
     * 修改总数
     */
//    public void updateClassTotal() {
//        System.out.println("呜呜呜呜呜w");
//        checkMapper.updateClassTotal();
//    }

    public void updateClassId() {
        checkMapper.updateClassId();
    }

    /**
     * 根据ID查询
     */
    public Check selectById(Integer id) {
        return checkMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Check> selectAll() {
        return checkMapper.selectCheck();
    }

//    /**
//     * 分页查询
//     */
//    public PageInfo<Check> selectPage(Check check, Integer pageNum, Integer pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        Account currentUser = TokenUtils.getCurrentUser();
//        if(!RoleEnum.ADMIN.toString().equals(currentUser.getRole())){
//            List<Check> list = checkMapper.selectUser(check);
//            return PageInfo.of(list);
//        }
//        List<Check> list = checkMapper.selectAll(check);
//        return PageInfo.of(list);
//    }
    /**
     * 分页查询
     */
    public PageInfo<Check> selectPage(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
//        Account currentUser = TokenUtils.getCurrentUser();
//        if(!RoleEnum.ADMIN.toString().equals(currentUser.getRole())){
//            List<Check> list = checkMapper.selectUser(name);
//            return PageInfo.of(list);
//        }
//        checkMapper.updateClassId();
        String currentUserRole = params.getRole();
        if(RoleEnum.TEACHER.toString().equals(currentUserRole)){
            List<Check> list = checkMapper.selectTeacher(params);
            return PageInfo.of(list);
        }
        else if(RoleEnum.TEACHER1.toString().equals(currentUserRole)){
            List<Check> list = checkMapper.selectTeacher1(params);
            return PageInfo.of(list);
        }
        else{
            List<Check> list = checkMapper.selectAll(params);
            return PageInfo.of(list);
        }
    }

}