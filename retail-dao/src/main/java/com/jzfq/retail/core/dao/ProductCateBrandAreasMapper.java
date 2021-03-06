package com.jzfq.retail.core.dao;

import com.jzfq.retail.bean.domain.ProductCateBrandAreas;
import com.jzfq.retail.bean.domain.ProductCateBrandAreasQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductCateBrandAreasMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int countByExample(ProductCateBrandAreasQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int deleteByExample(ProductCateBrandAreasQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int insert(ProductCateBrandAreas record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int insertSelective(ProductCateBrandAreas record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    List<ProductCateBrandAreas> selectByExample(ProductCateBrandAreasQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    ProductCateBrandAreas selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int updateByExampleSelective(@Param("record") ProductCateBrandAreas record, @Param("example") ProductCateBrandAreasQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int updateByExample(@Param("record") ProductCateBrandAreas record, @Param("example") ProductCateBrandAreasQuery example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int updateByPrimaryKeySelective(ProductCateBrandAreas record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_cate_brand_areas
     *
     * @mbggenerated Thu Aug 02 14:38:31 CST 2018
     */
    int updateByPrimaryKey(ProductCateBrandAreas record);
}