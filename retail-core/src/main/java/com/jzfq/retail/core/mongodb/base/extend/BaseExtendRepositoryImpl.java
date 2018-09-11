package com.jzfq.retail.core.mongodb.base.extend;//package com.retail.mongodb.base.extend;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.github.pagehelper.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.data.repository.NoRepositoryBean;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * log统一接口实现类
// * llli
// * 2017-06-05
// */
//@Slf4j
//@NoRepositoryBean
//public class BaseExtendRepositoryImpl<E, ID extends Serializable> implements BaseExtendRepository<E,ID> {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public void save(E entities) {
//        mongoTemplate.save(entities);
//    }
//
//    @Override
//    public <E> E queryByRegex(Query query, Class<E> entityClass) {
//        return mongoTemplate.findOne(query, entityClass);
//    }
//
//    @Override
//    public <E> E queryByLte(Query query, Class<E> entityClass) {
//       return mongoTemplate.findOne(query,entityClass);
//    }
//
//    @Override
//    public void update(Query query, Update update, String table) {
//        mongoTemplate.updateMulti(query,update,table);
//    }
//
//    @Override
//    public List<E> findAll(Class<E> entityClass) {
//        return mongoTemplate.findAll(entityClass);
//    }
//
//    @Override
//    public Page findAll(Pageable var1) {
//        return null;
//    }
//
//    @Override
//    public void delete(Query query, Class<E> entityClass) {
//        mongoTemplate.remove(query,entityClass);
//    }
//
//    @Override
//    public Long count(Query query, Class<E> entityClass) {
//        return mongoTemplate.count(query,entityClass);
//    }
//
//    @Override
//    public List<E> page(Query query, Class<E> entityClass) {
//        List<E> datas = mongoTemplate.find(query,entityClass);
//        return datas;
//
//    }
//}
