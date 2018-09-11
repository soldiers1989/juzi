package com.jzfq.retail.core.mongodb.base;//package com.retail.mongodb.base;
//
//import com.retail.mongodb.support.BatchUpdateOptions;
//import com.mongodb.gridfs.GridFSDBFile;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.repository.NoRepositoryBean;
//
//import java.io.InputStream;
//import java.io.Serializable;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author lagon
// * @time 2017/2/17 18:07
// * @description MongoDB基础Repository
// */
//@NoRepositoryBean
//public interface BaseRepository<E,ID extends Serializable> extends MongoRepository<E, ID> {
//
//    //以字节数组形式上传文件到MongoDB GridFS系统中
//    void uploadFileToGridFS(byte[] content, String fileName, String contentType, Map<String, Object> metamap);
//
//    //以流形式上传文件到MongoDB GridFS系统中
//    void uploadFileToGridFS(InputStream inputStream, String fileName, String contentType, Map<String, Object> metamap);
//
//    //从MongoDB GridFS中获取文件
//    GridFSDBFile getFileFromGridFS(Query query);
//
//    //从MongoDB GridFS中删除文件
//    void removeFileFromGridFS(Query query);
//
//    int batchUpdate(String collName, List<BatchUpdateOptions> options, boolean ordered);
//    int batchUpdate(Class<?> entityClass, List<BatchUpdateOptions> options, boolean ordered);
//    int batchUpdate(String collName, List<BatchUpdateOptions> options);
//    int batchUpdate(Class<?> entityClass, List<BatchUpdateOptions> options);
//
//}
