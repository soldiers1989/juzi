package com.jzfq.retail.core.mongodb.support;//package com.retail.mongodb.support;
//
//import com.retail.mongodb.SequenceId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.FindAndModifyOptions;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
//import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.reflect.Field;
//
///**
// * @author lagon
// * @time 2017/2/18 10:04
// * @description 自动注入ID监听器
// */
//public class SaveMongoEventListener extends AbstractMongoEventListener<Object> {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
//        Object source=event.getSource();
//        if(source != null) {
//            ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {
//                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
//                    ReflectionUtils.makeAccessible(field);
//                    //注解GeneratedValue且属性值为0时生成新的ID
//                    if (field.isAnnotationPresent(GeneratedValue.class)&&(long)field.get(source)==0L) {
//                        //设置自增ID
//                        field.set(source, getNextId(source.getClass().getSimpleName()));
//                    }
//                }
//            });
//        }
//    }
//
//    /**
//     * 获取下一个自增ID
//     * @param collName  集合名
//     * @return
//     */
//    private Long getNextId(String collName) {
//        Query query = new Query(Criteria.where("collName").is(collName));
//        Update update = new Update();
//        update.inc("seqId", 1);
//        FindAndModifyOptions options = new FindAndModifyOptions();
//        options.upsert(true);
//        options.returnNew(true);
//        //findAndModify()是原子操作，所以不用担心并发问题
//        SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
//        return seqId.getSeqId();
//    }
//}
