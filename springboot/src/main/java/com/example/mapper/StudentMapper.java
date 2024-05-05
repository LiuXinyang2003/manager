package com.example.mapper;

import com.example.entity.Params;
import com.example.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 操作student相关数据接口
*/
public interface StudentMapper {

    /**
      * 新增
    */
    int insert(Student student);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Student student);

    /**
      * 根据ID查询
    */
    Student selectById(Integer id);

    /**
      * 查询所有
    */
    List<Student> selectStudent(Student student);

    List<Student> findAll();

    List<Student> selectAll(@Param("params") Params params);

    List<Student> selectTeacher(@Param("params") Params params);



    @Select("select * from student where username = #{username}")
    Student selectByUsername(String username);

    Student selectAdd(String name);

}