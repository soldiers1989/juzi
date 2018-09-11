package com.jzfq.retail.core.mongodb.base;//package com.retail.mongodb.base;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBObject;
//import com.mongodb.gridfs.GridFSDBFile;
//import com.mongodb.gridfs.GridFSFile;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.gridfs.GridFsTemplate;
//import org.springframework.data.repository.NoRepositoryBean;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.Map;
//
///**
// * @author lagon
// * @time 2017/2/17 18:07
// * @description GridFS操作支撑类
// */
//@Slf4j
//@NoRepositoryBean
//public class GridFsSupport {
//
//    @Autowired
//    protected GridFsTemplate gridFsTemplate;
//
//    @Autowired
//    private ThreadPoolTaskExecutor taskExecutor;
//
//    //以字节数组形式上传文件到MongoDB GridFS系统中
//    public void uploadFileToGridFS(byte[] content,String fileName,String contentType,Map<String,Object> metamap) {
//        taskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                //存储文件的额外信息，比如用户ID,后面要查询某个用户的所有文件时就可以直接查询
//                DBObject metadata = new BasicDBObject(metamap);
//                GridFSFile gridFSFile = gridFsTemplate.store(new ByteArrayInputStream(content), fileName, contentType, metadata);
//                String fileId = gridFSFile.getId().toString();
//                log.info("成功保存文件到MongoDB GridFS系统中,file_id:{}",fileId);
//            }
//        });
//    }
//
//    //以流形式上传文件到MongoDB GridFS系统中
//    public void uploadFileToGridFS(InputStream inputStream, String fileName, String contentType, Map<String,Object> metamap) {
//        taskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                //存储文件的额外信息，比如用户ID,后面要查询某个用户的所有文件时就可以直接查询
//                DBObject metadata = new BasicDBObject(metamap);
//                GridFSFile gridFSFile = gridFsTemplate.store(inputStream, fileName, contentType, metadata);
//                String fileId = gridFSFile.getId().toString();
//                log.info("成功保存文件到MongoDB GridFS系统中,file_id:{}",fileId);
//            }
//        });
//    }
//
//    //从MongoDB GridFS中获取文件
//    public GridFSDBFile getFileFromGridFS(Query query) {
//        return gridFsTemplate.findOne(query);
//    }
//
//    //从MongoDB GridFS中删除文件
//    public void removeFileFromGridFS(Query query) {
//        gridFsTemplate.delete(query);
//    }
//
//
//}
