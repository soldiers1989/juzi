package com.jzfq.retail.core.config.mongodb;//package .config.mongodb;
//
//import .mongodb.support.SaveMongoEventListener;
//import com.mongodb.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
//import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
//import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
//import org.springframework.data.mongodb.gridfs.GridFsTemplate;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author lagon
// * @time 2017/2/17 14:32
// * @description MongoDB配置类
// */
////@Configuration
////@EnableMongoRepositories(basePackages = "com.retail.mongodb.operation")
//public class MongoConfig extends AbstractMongoConfiguration {
//
//    @Value("${spring.data.mongodb.server.address}")
//    private String serverAddress;
//    @Value("${spring.data.mongodb.server.username}")
//    private String username;
//    @Value("${spring.data.mongodb.server.password}")
//    private String password;
//    @Value("${spring.data.mongodb.server.dbname}")
//    private String dbname;
//
//    @Override
//    //指定映射实体类的扫描路径
//    public String getMappingBasePackage() {
//        return "com.yingu.contract.processing.center.mongodb.domain";
//    }
//
//    @Override
//    //指定使用的mongodb数据库
//    protected String getDatabaseName() {
//        return dbname;
//    }
//
//    @Override
//    public Mongo mongo() throws Exception {
//        MongoClientOptions clientOptions = new MongoClientOptions.Builder()
//                .connectionsPerHost(30)
//                .minConnectionsPerHost(10)
//                .maxWaitTime(2000)
//                .writeConcern(WriteConcern.W1) //写关注
//                .build();
//        List<MongoCredential> credentialsList = Arrays.asList(MongoCredential.createMongoCRCredential(
//                username, dbname, password.toCharArray()));
//        List<ServerAddress> seeds=new ArrayList<ServerAddress>();
//        String[] serverAddresses=serverAddress.split(",");
//        for(String sa:serverAddresses){
//            String[] perServer=sa.split(":");
//            seeds.add(new ServerAddress(perServer[0],Integer.valueOf(perServer[1])));
//        }
//        return new MongoClient(seeds,clientOptions);
//    }
//
//    @Override
//    public MappingMongoConverter mappingMongoConverter() throws Exception {
//        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
//        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext());
//        //这里设置为空,可以把 spring data mongodb 多余保存的_class字段去掉
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return converter;
//    }
//
//    @Override
//    public MongoTemplate mongoTemplate() throws Exception {
//        return new MongoTemplate(mongoDbFactory(),mappingMongoConverter());
//    }
//
//    @Bean
//    public GridFsTemplate gridFsTemplate() throws Exception {
//        return new GridFsTemplate(mongoDbFactory(),mappingMongoConverter());
//    }
//
//    @Bean
//    public SaveMongoEventListener saveMongoEventListener(){
//        return new SaveMongoEventListener();
//    }
//
//}
