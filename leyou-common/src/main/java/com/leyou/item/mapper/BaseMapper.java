package com.leyou.item.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 *  自定义BaseMapper<T>接口，继承通用Mapper的Mapper<T>，IdListMapper<T,pk>，InsertListMapper<T>三个接口
 *      <pk>: PrimaryKey 持久层接口对应实体类主键的数据类型
 *   Mapper<T>：单表基本的CRUD的接口
 *   IdListMapper<T,pk>: 扩展了如下两个方法：
 *    1. List<T> selectByIdList(@Param("idList") List<PK> var1) 根据给定的主键list集合查询出这些主键对应的数据并封装到list集合中
 *    2. int deleteByIdList(@Param("idList") List<PK> var1) 批量删除参数指定的list集合中这些主键对应的数据
 *   InsertListMapper<T>: 扩展了一个方法：
 *    int insertList(List<? extends T> recordList)：批量插入参数List集合中的数据
 *   需要注意的是：InsertListMapper<T>这个接口在通用Mapper的common包下和additional包下都有，common包下的insertListMapper接口
 *   要求参数集合中对象的主键属性名必须是Id。而additional包的InsertListMapper接口却没有这个要求。
 * @param <T>  继承该接口的持久层接口对应的实体类
 * @param <pk> 实体类主键的数据类型
 */
@RegisterMapper
public interface BaseMapper<T,pk> extends Mapper<T>, IdListMapper<T,pk>, InsertListMapper<T> {
}
