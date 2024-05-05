package com.example.mapper;

import com.example.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作check相关数据接口
*/
public interface CheckMapper {

    /**
      * 新增
    */
    int insert(Check check);

    int signAdd(Check check);

    int signAdd2(Check check,String studentName);
    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Check check);

    int updateClassId();

    /**
     * 修改总数
     */
    int updateClassTotal();

    /**
      * 根据ID查询
    */
    Check selectById(Integer id);

    /**
      * 查询所有
    */
//    List<Check> selectAll(Check check);
    List<Check> selectCheck();

    List<Check> selectAll(@Param("params") Params params);

    List<Check> selectTeacher(@Param("params") Params params);

    List<Check> selectClasses(Classes classes);
//    List<Check> selectUser(Check check);
List<Check> selectUser(String name);

}