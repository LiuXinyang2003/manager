package com.example.mapper;

import com.example.entity.Params;
import com.example.entity.Teacher1;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作teacher1相关数据接口
*/
public interface Teacher1Mapper {

    /**
      * 新增
    */
    int insert(Teacher1 teacher1);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Teacher1 teacher1);

    int updateClassId(Teacher1 teacher1);

    /**
      * 根据ID查询
    */
    Teacher1 selectById(Integer id);

    /**
      * 查询所有
    */
    List<Teacher1> selectTeacher(Teacher1 teacher1);

    List<Teacher1> selectAll(@Param("params") Params params);

    @Select("select * from teacher1 where username = #{username}")
    Teacher1 selectByUsername(String username);
}