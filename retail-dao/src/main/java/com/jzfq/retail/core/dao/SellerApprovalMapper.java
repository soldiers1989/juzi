package com.jzfq.retail.core.dao;

import com.jzfq.retail.bean.domain.SellerApproval;
import com.jzfq.retail.bean.domain.SellerApprovalQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SellerApprovalMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int countByExample(SellerApprovalQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int deleteByExample(SellerApprovalQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int insert(SellerApproval record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int insertSelective(SellerApproval record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    List<SellerApproval> selectByExample(SellerApprovalQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    SellerApproval selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int updateByExampleSelective(@Param("record") SellerApproval record, @Param("example") SellerApprovalQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int updateByExample(@Param("record") SellerApproval record, @Param("example") SellerApprovalQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int updateByPrimaryKeySelective(SellerApproval record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller_approval
     *
     * @mbggenerated Wed Jul 18 14:16:10 CST 2018
     */
    int updateByPrimaryKey(SellerApproval record);
}