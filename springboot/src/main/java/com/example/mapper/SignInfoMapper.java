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

    int signAdd(SignInfo signInfo);


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


}