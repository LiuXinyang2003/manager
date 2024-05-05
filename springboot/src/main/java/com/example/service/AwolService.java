package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Awol;
import com.example.entity.Params;
import com.example.mapper.AwolMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 请假信息表业务处理
 **/
@Service
public class AwolService {

    @Resource
    private AwolMapper awolMapper;

    /**
     * 新增
     */
    public void add(Awol awol) {
        awolMapper.insert(awol);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        awolMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            awolMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Awol awol) {
        awolMapper.updateById(awol);
    }


    public void updateClassId() {
        awolMapper.updateClassId();
    }

    /**
     * 根据ID查询
     */
    public Awol selectById(Integer id) {
        return awolMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Awol> selectAll() {
        return awolMapper.selectAwol();
    }

//    /**
//     * 分页查询
//     */
//    public PageInfo<Awol> selectPage(Awol awol, Integer pageNum, Integer pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        Account currentUser = TokenUtils.getCurrentUser();
//        if(!RoleEnum.ADMIN.toString().equals(currentUser.getRole())){
//            List<Awol> list = awolMapper.selectUser(awol);
//            return PageInfo.of(list);
//        }
//        List<Awol> list = awolMapper.selectAll(awol);
//        return PageInfo.of(list);
//    }
    /**
     * 分页查询
     */
    public PageInfo<Awol> selectPage(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        String currentUserRole = params.getRole();
        System.out.println("法大师傅大师傅"+params.getRole());
        if(RoleEnum.STUDENT.toString().equals(currentUserRole)){
            List<Awol> list = awolMapper.selectStudent(params);
            return PageInfo.of(list);
        }
        else if(RoleEnum.TEACHER.toString().equals(currentUserRole)){
            List<Awol> list = awolMapper.selectTeacher(params);
            return PageInfo.of(list);
        }
        else{
            List<Awol> list = awolMapper.selectAll(params);
            return PageInfo.of(list);
        }
    }

}