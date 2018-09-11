package com.jzfq.retail.core.mongodb.base;//package com.retail.mongodb.base;
//
//import com.retail.mongodb.support.BatchUpdateOptions;
//import com.mongodb.BasicDBObject;
//import com.mongodb.CommandResult;
//import com.mongodb.DBCollection;
//import com.mongodb.DBObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.InvalidDataAccessApiUsageException;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.repository.NoRepositoryBean;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author lagon
// * @time 2017/2/18 18:43
// * @description 批量操作支撑类
// */
//@NoRepositoryBean
//public class BatchSupport extends GridFsSupport {
//
//    @Autowired
//    protected MongoTemplate mongoTemplate;
//
//    /**
//     * 批量更新
//     * @param ordered 如果为true,一条语句更新失败，剩下的语句将不再执。如果为false,一条语句更新失败，剩下的将继续执行。默认为true。
//     */
//    public int batchUpdate(String collName, List<BatchUpdateOptions> options, boolean ordered) {
//        return doBatchUpdate(collName, options, ordered);
//    }
//
//    public int batchUpdate(Class<?> entityClass, List<BatchUpdateOptions> options, boolean ordered) {
//        String collName = determineCollectionName(entityClass);
//        return batchUpdate(collName,options,ordered);
//    }
//
//    public int batchUpdate(String collName, List<BatchUpdateOptions> options) {
//        return batchUpdate(collName,options,true);
//    }
//
//    public int batchUpdate(Class<?> entityClass, List<BatchUpdateOptions> options) {
//        String collectionName = determineCollectionName(entityClass);
//        return batchUpdate(entityClass,options,true);
//    }
//
//    private int doBatchUpdate(String collName, List<BatchUpdateOptions> options, boolean ordered) {
//        DBCollection dbCollection=mongoTemplate.getCollection(collName);
//        DBObject command = new BasicDBObject();
//        command.put("update", collName);
//        List<BasicDBObject> updateList = new ArrayList<BasicDBObject>();
//        for (BatchUpdateOptions option : options) {
//            BasicDBObject update = new BasicDBObject();
//            update.put("q", option.getQuery().getQueryObject());
//            update.put("u", option.getUpdate().getUpdateObject());
//            update.put("upsert", option.isUpsert());
//            update.put("multi", option.isMulti());
//            updateList.add(update);
//        }
//        command.put("updates", updateList);
//        command.put("ordered", ordered);
//        CommandResult commandResult = dbCollection.getDB().command(command);
//        return Integer.parseInt(commandResult.get("n").toString());
//    }
//
//    private static String determineCollectionName(Class<?> entityClass) {
//        if (entityClass == null) {
//            throw new InvalidDataAccessApiUsageException(
//                    "No class parameter provided, entity collection can't be determined!");
//        }
//        String collName = entityClass.getSimpleName();
//        if(entityClass.isAnnotationPresent(Document.class)) {
//            Document document = entityClass.getAnnotation(Document.class);
//            collName = document.collection();
//        } else {
//            collName = collName.replaceFirst(collName.substring(0, 1),collName.substring(0, 1).toLowerCase()) ;
//        }
//        return collName;
//    }
//
//}
