package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.SignInfoMapper;
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
public class SignInfoService {

    @Resource
    private SignInfoMapper signInfoMapper;



    public List<SignInfo> findAll(String role,Integer classId,String name,String name2,String classNameP,String username) {
        System.out.println("role的值"+role);
        if(RoleEnum.TEACHER.toString().equals(role)){
            System.out.println("进入Service层"+role);
            List<SignInfo> list = signInfoMapper.selectTeacher2(classId,name,name2);
            return list;
        }
        if(RoleEnum.TEACHER1.toString().equals(role)){
            List<SignInfo> list = signInfoMapper.selectTeacher11(role,name,name2,username);
            return list;
        }
        else{
            List<SignInfo> list = signInfoMapper.selectAll2(name,name2,classNameP);
            return list;
        }
    }
    /**
     * 新增
     */
    public void add(SignInfo signInfo) {
        signInfoMapper.insert(signInfo);
    }



    /**
     * 删除
     */
    public void deleteById(Integer id) {
        signInfoMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            signInfoMapper.deleteById(id);
        }
    }

    public void updateClassId(SignInfo signInfo) {
        signInfoMapper.updateClassId(signInfo);
    }

    public void updateState() {
        signInfoMapper.updateState();
    }

    public void updateById(SignInfo signInfo) {
        signInfoMapper.updateById(signInfo);
    }

    /**
     * 根据ID查询
     */
    public SignInfo selectById(Integer id) {
        return signInfoMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<SignInfo> selectAll() {
        return signInfoMapper.selectSignInfo();
    }

//    /**
//     * 分页查询
//     */
//    public PageInfo<SignInfo> selectPage(SignInfo signInfo, Integer pageNum, Integer pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        Account currentUser = TokenUtils.getCurrentUser();
//        if(!RoleEnum.ADMIN.toString().equals(currentUser.getRole())){
//            List<SignInfo> list = signInfoMapper.selectUser(signInfo);
//            return PageInfo.of(list);
//        }
//        List<SignInfo> list = signInfoMapper.selectAll(signInfo);
//        return PageInfo.of(list);
//    }
    /**
     * 分页查询
     */
    public PageInfo<SignInfo> selectPage(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        String currentUserRole = params.getRole();
        if(RoleEnum.STUDENT.toString().equals(currentUserRole)){
            List<SignInfo> list = signInfoMapper.selectStudent(params);
            return PageInfo.of(list);
        }
        else if(RoleEnum.TEACHER.toString().equals(currentUserRole)){
            List<SignInfo> list = signInfoMapper.selectTeacher(params);
            return PageInfo.of(list);
        }
        else if(RoleEnum.TEACHER1.toString().equals(currentUserRole)){
            List<SignInfo> list = signInfoMapper.selectTeacher1(params);
            return PageInfo.of(list);
        }
        else{
            List<SignInfo> list = signInfoMapper.selectAll(params);
            return PageInfo.of(list);
        }
    }

}