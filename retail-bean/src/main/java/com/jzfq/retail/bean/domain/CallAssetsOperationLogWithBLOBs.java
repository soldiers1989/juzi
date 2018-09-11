package com.jzfq.retail.bean.domain;

import java.io.Serializable;

public class CallAssetsOperationLogWithBLOBs extends CallAssetsOperationLog implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column call_assets_operation_log.request_param
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private String requestParam;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column call_assets_operation_log.response_param
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private String responseParam;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table call_assets_operation_log
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column call_assets_operation_log.request_param
     *
     * @return the value of call_assets_operation_log.request_param
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public String getRequestParam() {
        return requestParam;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column call_assets_operation_log.request_param
     *
     * @param requestParam the value for call_assets_operation_log.request_param
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam == null ? null : requestParam.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column call_assets_operation_log.response_param
     *
     * @return the value of call_assets_operation_log.response_param
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public String getResponseParam() {
        return responseParam;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column call_assets_operation_log.response_param
     *
     * @param responseParam the value for call_assets_operation_log.response_param
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    public void setResponseParam(String responseParam) {
        this.responseParam = responseParam == null ? null : responseParam.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table call_assets_operation_log
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
        CallAssetsOperationLogWithBLOBs other = (CallAssetsOperationLogWithBLOBs) that;
        return (this.getMacId() == null ? other.getMacId() == null : this.getMacId().equals(other.getMacId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getServiceType() == null ? other.getServiceType() == null : this.getServiceType().equals(other.getServiceType()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getRequestTime() == null ? other.getRequestTime() == null : this.getRequestTime().equals(other.getRequestTime()))
            && (this.getResponseTime() == null ? other.getResponseTime() == null : this.getResponseTime().equals(other.getResponseTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRequestParam() == null ? other.getRequestParam() == null : this.getRequestParam().equals(other.getRequestParam()))
            && (this.getResponseParam() == null ? other.getResponseParam() == null : this.getResponseParam().equals(other.getResponseParam()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table call_assets_operation_log
     *
     * @mbggenerated Thu Jul 05 16:20:00 CST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMacId() == null) ? 0 : getMacId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getServiceType() == null) ? 0 : getServiceType().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getRequestTime() == null) ? 0 : getRequestTime().hashCode());
        result = prime * result + ((getResponseTime() == null) ? 0 : getResponseTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRequestParam() == null) ? 0 : getRequestParam().hashCode());
        result = prime * result + ((getResponseParam() == null) ? 0 : getResponseParam().hashCode());
        return result;
    }
}