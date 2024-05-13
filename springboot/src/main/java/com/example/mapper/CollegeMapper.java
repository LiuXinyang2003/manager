package com.example.mapper;

import com.example.entity.College;
import com.example.entity.Params;
import com.example.entity.Speciality;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作college相关数据接口
*/
public interface CollegeMapper {

    /**
      * 新增
    */
    int insert(College college);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(College college);

    /**
      * 根据ID查询
    */
    College selectById(Integer id);

    /**
      * 查询所有
    */
    List<College> selectAll();

    List<College> findBySearch(@Param("params") Params params);

    List<Speciality> getSpecialtiesByCollegeId(Integer collegeId);
}