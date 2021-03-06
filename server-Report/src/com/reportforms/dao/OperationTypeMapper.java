package com.reportforms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reportforms.model.OperationType;
import com.reportforms.model.OperationTypeExample;

public interface OperationTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int countByExample(OperationTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int deleteByExample(OperationTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int insert(OperationType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int insertSelective(OperationType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    List<OperationType> selectByExample(OperationTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    OperationType selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int updateByExampleSelective(@Param("record") OperationType record, @Param("example") OperationTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int updateByExample(@Param("record") OperationType record, @Param("example") OperationTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int updateByPrimaryKeySelective(OperationType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_operation_type
     *
     * @mbggenerated Tue Dec 20 22:33:15 CST 2011
     */
    int updateByPrimaryKey(OperationType record);
}