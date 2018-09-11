package com.jzfq.retail.bean.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductCate implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.product_type_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Integer productTypeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.pid
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Integer pid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.name
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.path
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private String path;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.scaling
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private BigDecimal scaling;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.create_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Integer createId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.update_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Integer updateId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.create_time
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.update_time
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.sort
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Integer sort;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.image
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private String image;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.visible
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Integer visible;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_cate.type
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table product_cate
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.id
     *
     * @return the value of product_cate.id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.id
     *
     * @param id the value for product_cate.id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.product_type_id
     *
     * @return the value of product_cate.product_type_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Integer getProductTypeId() {
        return productTypeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.product_type_id
     *
     * @param productTypeId the value for product_cate.product_type_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.pid
     *
     * @return the value of product_cate.pid
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.pid
     *
     * @param pid the value for product_cate.pid
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.name
     *
     * @return the value of product_cate.name
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.name
     *
     * @param name the value for product_cate.name
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.path
     *
     * @return the value of product_cate.path
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public String getPath() {
        return path;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.path
     *
     * @param path the value for product_cate.path
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.scaling
     *
     * @return the value of product_cate.scaling
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public BigDecimal getScaling() {
        return scaling;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.scaling
     *
     * @param scaling the value for product_cate.scaling
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setScaling(BigDecimal scaling) {
        this.scaling = scaling;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.create_id
     *
     * @return the value of product_cate.create_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Integer getCreateId() {
        return createId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.create_id
     *
     * @param createId the value for product_cate.create_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.update_id
     *
     * @return the value of product_cate.update_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Integer getUpdateId() {
        return updateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.update_id
     *
     * @param updateId the value for product_cate.update_id
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.create_time
     *
     * @return the value of product_cate.create_time
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.create_time
     *
     * @param createTime the value for product_cate.create_time
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.update_time
     *
     * @return the value of product_cate.update_time
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.update_time
     *
     * @param updateTime the value for product_cate.update_time
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.sort
     *
     * @return the value of product_cate.sort
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.sort
     *
     * @param sort the value for product_cate.sort
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.image
     *
     * @return the value of product_cate.image
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public String getImage() {
        return image;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.image
     *
     * @param image the value for product_cate.image
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.visible
     *
     * @return the value of product_cate.visible
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Integer getVisible() {
        return visible;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.visible
     *
     * @param visible the value for product_cate.visible
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_cate.type
     *
     * @return the value of product_cate.type
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_cate.type
     *
     * @param type the value for product_cate.type
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate
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
        ProductCate other = (ProductCate) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProductTypeId() == null ? other.getProductTypeId() == null : this.getProductTypeId().equals(other.getProductTypeId()))
            && (this.getPid() == null ? other.getPid() == null : this.getPid().equals(other.getPid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getScaling() == null ? other.getScaling() == null : this.getScaling().equals(other.getScaling()))
            && (this.getCreateId() == null ? other.getCreateId() == null : this.getCreateId().equals(other.getCreateId()))
            && (this.getUpdateId() == null ? other.getUpdateId() == null : this.getUpdateId().equals(other.getUpdateId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSort() == null ? other.getSort() == null : this.getSort().equals(other.getSort()))
            && (this.getImage() == null ? other.getImage() == null : this.getImage().equals(other.getImage()))
            && (this.getVisible() == null ? other.getVisible() == null : this.getVisible().equals(other.getVisible()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate
     *
     * @mbggenerated Wed Jun 27 16:29:15 CST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProductTypeId() == null) ? 0 : getProductTypeId().hashCode());
        result = prime * result + ((getPid() == null) ? 0 : getPid().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getScaling() == null) ? 0 : getScaling().hashCode());
        result = prime * result + ((getCreateId() == null) ? 0 : getCreateId().hashCode());
        result = prime * result + ((getUpdateId() == null) ? 0 : getUpdateId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        result = prime * result + ((getImage() == null) ? 0 : getImage().hashCode());
        result = prime * result + ((getVisible() == null) ? 0 : getVisible().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }
}