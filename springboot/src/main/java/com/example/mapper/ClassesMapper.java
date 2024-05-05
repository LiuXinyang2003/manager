package com.example.mapper;

import com.example.entity.Classes;
import com.example.entity.Params;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作classes相关数据接口
*/
public interface ClassesMapper {

    /**
      * 新增
    */
    int insert(Classes classes);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Classes classes);

    /**
     * 修改总数
     */
    int updateClassTotal(Classes classes);

    /**
      * 根据ID查询
    */
    Classes selectById(Integer id);

    /**
      * 查询所有
    */
    List<Classes> selectClass(Classes classes);

    List<Classes> selectAll(@Param("params") Params params);

}