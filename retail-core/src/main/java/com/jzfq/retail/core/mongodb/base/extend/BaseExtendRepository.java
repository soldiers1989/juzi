package com.jzfq.retail.core.mongodb.base.extend;//package com.retail.mongodb.base.extend;
//
//import com.github.pagehelper.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.data.repository.NoRepositoryBean;
//import org.springframework.data.repository.Repository;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * 定义公共的接口
// * llli
// * 2017-06-05
// */
//@NoRepositoryBean
//public interface BaseExtendRepository<T, ID extends Serializable> extends Repository<T, ID> {
//
//
//    /**
//     * 保存
//     * @param entities
//     */
//    void save(T entities);
//
//    /**
//     * regex查询
//     * @param query
//     * @param entityClass
//     */
//    <T> T queryByRegex(Query query, Class<T> entityClass);
//
//    /**
//     * lte查询
//     * @param query
//     * @param entityClass
//     */
//    <T> T queryByLte(Query query, Class<T> entityClass);
//
//    /**
//     * 根据表的特定字段做更新操作
//     * @param query
//     * @param update
//     * @param table
//     */
//    void update(Query query, Update update, String table);
//
//    /**
//     * 查询全部内容
//     * @param entityClass
//     * @return
//     */
//    List<T> findAll(Class<T> entityClass);
//
//    /**
//     * 分页查询
//     * @param var1
//     * @return
//     */
//    Page<T> findAll(Pageable var1);
//
//    /**
//     * 删除操作
//     * @param entityClass
//     */
//    void  delete(Query query, Class<T> entityClass);
//
//    /**
//     * 根据条件查询记录数(分页)
//     * @param query
//     * @return
//     */
//    Long count(Query query, Class<T> entityClass);
//
//    /**
//     * 分页查询
//     * @param query
//     * @param entityClass
//     * @return
//     */
//    List<T> page(Query query, Class<T> entityClass);
//
//
//}
