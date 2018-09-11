package com.jzfq.retail.core.dao;

import com.jzfq.retail.bean.domain.OrderPayLog;
import com.jzfq.retail.bean.domain.OrderPayLogQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderPayLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int countByExample(OrderPayLogQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int deleteByExample(OrderPayLogQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int insert(OrderPayLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int insertSelective(OrderPayLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    List<OrderPayLog> selectByExample(OrderPayLogQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    OrderPayLog selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int updateByExampleSelective(@Param("record") OrderPayLog record, @Param("example") OrderPayLogQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int updateByExample(@Param("record") OrderPayLog record, @Param("example") OrderPayLogQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int updateByPrimaryKeySelective(OrderPayLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    int updateByPrimaryKey(OrderPayLog record);
}