package com.example.mapper;

import com.example.entity.Awol;
import com.example.entity.Params;
import com.example.entity.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作teacher相关数据接口
*/
public interface TeacherMapper {

    /**
      * 新增
    */
    int insert(Teacher teacher);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Teacher teacher);

    int updateClassId(Teacher teacher);

    /**
      * 根据ID查询
    */
    Teacher selectById(Integer id);

    /**
      * 查询所有
    */
    List<Teacher> selectTeacher(Teacher teacher);

    List<Teacher> selectAll(@Param("params") Params params);

    @Select("select * from teacher where username = #{username}")
    Teacher selectByUsername(String username);
}