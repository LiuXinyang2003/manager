package com.example.mapper;

import com.example.entity.Awol;
import com.example.entity.Params;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作awol相关数据接口
*/
public interface AwolMapper {

    /**
      * 新增
    */
    int insert(Awol awol);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Awol awol);


    int updateClassId();

    /**
      * 根据ID查询
    */
    Awol selectById(Integer id);

    /**
      * 查询所有
    */
//    List<Awol> selectAll(Awol awol);
    List<Awol> selectAwol();
    List<Awol> selectAll(@Param("params") Params params);

//    List<Awol> selectUser(Awol awol);
    List<Awol> selectStudent(@Param("params") Params params);

    List<Awol> selectTeacher(@Param("params") Params params);

}