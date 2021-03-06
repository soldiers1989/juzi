package com.jzfq.retail.bean.domain;

import java.io.Serializable;
import java.util.Date;

public class CallAccountsOperation implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column call_accounts_operation.mac_id
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private String macId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column call_accounts_operation.service_type
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private String serviceType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column call_accounts_operation.status
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column call_accounts_operation.create_time
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column call_accounts_operation.remark
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table call_accounts_operation
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column call_accounts_operation.mac_id
     *
     * @return the value of call_accounts_operation.mac_id
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public String getMacId() {
        return macId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column call_accounts_operation.mac_id
     *
     * @param macId the value for call_accounts_operation.mac_id
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public void setMacId(String macId) {
        this.macId = macId == null ? null : macId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column call_accounts_operation.service_type
     *
     * @return the value of call_accounts_operation.service_type
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column call_accounts_operation.service_type
     *
     * @param serviceType the value for call_accounts_operation.service_type
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column call_accounts_operation.status
     *
     * @return the value of call_accounts_operation.status
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column call_accounts_operation.status
     *
     * @param status the value for call_accounts_operation.status
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column call_accounts_operation.create_time
     *
     * @return the value of call_accounts_operation.create_time
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column call_accounts_operation.create_time
     *
     * @param createTime the value for call_accounts_operation.create_time
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column call_accounts_operation.remark
     *
     * @return the value of call_accounts_operation.remark
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column call_accounts_operation.remark
     *
     * @param remark the value for call_accounts_operation.remark
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table call_accounts_operation
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
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
        CallAccountsOperation other = (CallAccountsOperation) that;
        return (this.getMacId() == null ? other.getMacId() == null : this.getMacId().equals(other.getMacId()))
            && (this.getServiceType() == null ? other.getServiceType() == null : this.getServiceType().equals(other.getServiceType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table call_accounts_operation
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMacId() == null) ? 0 : getMacId().hashCode());
        result = prime * result + ((getServiceType() == null) ? 0 : getServiceType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}