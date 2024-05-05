package com.example.service;

import com.example.entity.College;
import com.example.entity.Params;
import com.example.entity.Speciality;
import com.example.mapper.CollegeMapper;
import com.example.mapper.SpecialityMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 专业信息表业务处理
 **/
@Service
public class SpecialityService {

    @Resource
    private SpecialityMapper specialityMapper;
    @Resource
    private CollegeMapper collegeMapper;

    /**
     * 新增
     */
    public void add(Speciality speciality) {
        specialityMapper.insert(speciality);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        specialityMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            specialityMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Speciality speciality) {
        specialityMapper.updateById(speciality);
    }

    /**
     * 根据ID查询
     */
    public Speciality selectById(Integer id) {
        return specialityMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Speciality> selectAll(Speciality speciality) {
        return specialityMapper.selectSpeciality(speciality);
    }

    public List<Speciality> getMajorsByCollege(Speciality speciality) {
        collegeMapper.selectAll();
//        for (int i =0;i<collegeMapper.selectAll().size();i++){
//            for (int j =0;j<specialityMapper.getMajorsByCollege(speciality).size();j++){
//                if(collegeMapper.selectAll().get(i).getId()==collegeMapper.selectAll().get(j).getCollegeId)
//            }
//        }
        System.out.println("专业数据"+collegeMapper.selectAll().get(0).getName());
        System.out.println("专业数据"+specialityMapper.getMajorsByCollege(speciality).get(1).getName());
        return specialityMapper.getMajorsByCollege(speciality);
    }
    /**
     * 分页查询
     */
    public PageInfo<Speciality> selectPage(Params params) {
        PageHelper.startPage(params.getPageNum(),params.getPageSize());
        List<Speciality> list = specialityMapper.selectAll(params);
        return PageInfo.of(list);
    }

}