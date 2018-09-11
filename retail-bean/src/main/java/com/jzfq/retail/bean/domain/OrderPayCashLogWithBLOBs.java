package com.jzfq.retail.bean.domain;

import java.io.Serializable;

public class OrderPayCashLogWithBLOBs extends OrderPayCashLog implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_pay_cash_log.pay_order_sn
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private String payOrderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_pay_cash_log.pay_content
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private String payContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_pay_cash_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_pay_cash_log.pay_order_sn
     *
     * @return the value of order_pay_cash_log.pay_order_sn
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public String getPayOrderSn() {
        return payOrderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_pay_cash_log.pay_order_sn
     *
     * @param payOrderSn the value for order_pay_cash_log.pay_order_sn
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setPayOrderSn(String payOrderSn) {
        this.payOrderSn = payOrderSn == null ? null : payOrderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_pay_cash_log.pay_content
     *
     * @return the value of order_pay_cash_log.pay_content
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public String getPayContent() {
        return payContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_pay_cash_log.pay_content
     *
     * @param payContent the value for order_pay_cash_log.pay_content
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setPayContent(String payContent) {
        this.payContent = payContent == null ? null : payContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_cash_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OrderPayCashLogWithBLOBs other = (OrderPayCashLogWithBLOBs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPaySn() == null ? other.getPaySn() == null : this.getPaySn().equals(other.getPaySn()))
            && (this.getTradeSn() == null ? other.getTradeSn() == null : this.getTradeSn().equals(other.getTradeSn()))
            && (this.getPaymentCode() == null ? other.getPaymentCode() == null : this.getPaymentCode().equals(other.getPaymentCode()))
            && (this.getPaymentName() == null ? other.getPaymentName() == null : this.getPaymentName().equals(other.getPaymentName()))
            && (this.getPayMoney() == null ? other.getPayMoney() == null : this.getPayMoney().equals(other.getPayMoney()))
            && (this.getMemberId() == null ? other.getMemberId() == null : this.getMemberId().equals(other.getMemberId()))
            && (this.getMemberName() == null ? other.getMemberName() == null : this.getMemberName().equals(other.getMemberName()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getPayOrderSn() == null ? other.getPayOrderSn() == null : this.getPayOrderSn().equals(other.getPayOrderSn()))
            && (this.getPayContent() == null ? other.getPayContent() == null : this.getPayContent().equals(other.getPayContent()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay_cash_log
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPaySn() == null) ? 0 : getPaySn().hashCode());
        result = prime * result + ((getTradeSn() == null) ? 0 : getTradeSn().hashCode());
        result = prime * result + ((getPaymentCode() == null) ? 0 : getPaymentCode().hashCode());
        result = prime * result + ((getPaymentName() == null) ? 0 : getPaymentName().hashCode());
        result = prime * result + ((getPayMoney() == null) ? 0 : getPayMoney().hashCode());
        result = prime * result + ((getMemberId() == null) ? 0 : getMemberId().hashCode());
        result = prime * result + ((getMemberName() == null) ? 0 : getMemberName().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getPayOrderSn() == null) ? 0 : getPayOrderSn().hashCode());
        result = prime * result + ((getPayContent() == null) ? 0 : getPayContent().hashCode());
        return result;
    }
}