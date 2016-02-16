package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mas.rave.main.vo.MenuType;
import com.mas.rave.main.vo.MenuTypeExample;

public interface MenuTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:15 CST 2011
     */
    int countByExample(MenuTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    int deleteByExample(MenuTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    int insert(MenuType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    int insertSelective(MenuType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    List<MenuType> selectByExample(MenuTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    MenuType selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    int updateByExampleSelective(@Param("record") MenuType record, @Param("example") MenuTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    int updateByExample(@Param("record") MenuType record, @Param("example") MenuTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    int updateByPrimaryKeySelective(MenuType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_menu_type
     *
     * @mbggenerated Tue Dec 20 22:26:16 CST 2011
     */
    int updateByPrimaryKey(MenuType record);
}