package com.example.service;

import com.example.entity.Classes;
import com.example.entity.Params;
import com.example.mapper.ClassesMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 学院信息表业务处理
 **/
@Service
public class ClassesService {

    @Resource
    private ClassesMapper classesMapper;

    /**
     * 新增
     */
    public void add(Classes classes) {
        classesMapper.insert(classes);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        classesMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            classesMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Classes classes) {
        classesMapper.updateById(classes);
    }

    /**
     * 修改总数
     */
    public void updateClassTotal(Classes classes) {
        classesMapper.updateClassTotal(classes);
    }

    /**
     * 根据ID查询
     */
    public Classes selectById(Integer id) {
        return classesMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Classes> selectAll(Classes classes) {
        return classesMapper.selectClass(classes);
    }

    /**
     * 分页查询
     */
    public PageInfo<Classes> selectPage(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        List<Classes> list = classesMapper.selectAll(params);
        return PageInfo.of(list);
    }

}