package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mas.rave.main.vo.RoleOperation;
import com.mas.rave.main.vo.RoleOperationExample;

public interface RoleOperationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int countByExample(RoleOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int deleteByExample(RoleOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int insert(RoleOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int insertSelective(RoleOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    List<RoleOperation> selectByExample(RoleOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    RoleOperation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int updateByExampleSelective(@Param("record") RoleOperation record, @Param("example") RoleOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int updateByExample(@Param("record") RoleOperation record, @Param("example") RoleOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int updateByPrimaryKeySelective(RoleOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role_operation
     *
     * @mbggenerated Wed Dec 21 09:31:04 CST 2011
     */
    int updateByPrimaryKey(RoleOperation record);
}