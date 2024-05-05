package com.example.mapper;

import com.example.entity.Admin;
import com.example.entity.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作notice相关数据接口
*/
public interface NoticeMapper {

    /**
      * 新增
    */
    int insert(Notice notice);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Notice notice);

    /**
      * 根据ID查询
    */
    Notice selectById(Integer id);

    /**
      * 查询所有
    */
    List<Notice> selectAll(Notice notice);

}