package com.example.mapper;

import com.example.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作signInfo相关数据接口
*/
public interface SignInfoMapper {

    /**
      * 新增
    */
    int insert(SignInfo signInfo);



    /**
      * 删除
    */
    int deleteById(Integer id);

    int updateClassId(SignInfo signInfo);

    int updateState();


    int updateById(SignInfo signInfo);
    /**
      * 根据ID查询
    */
    SignInfo selectById(Integer id);

    /**
      * 查询所有
    */
    List<SignInfo> selectSignInfo();

    List<SignInfo> selectAll(@Param("params") Params params);

    List<SignInfo> selectStudent(@Param("params") Params params);

    List<SignInfo> selectTeacher(@Param("params") Params params);

    List<SignInfo> selectTeacher1(@Param("params") Params params);

    List<SignInfo> selectAll2(String name,String name2,String classNameP);

    List<SignInfo> selectTeacher2(Integer classId,String name,String name2);

    List<SignInfo> selectTeacher11(String role,String name,String name2,String username);
}