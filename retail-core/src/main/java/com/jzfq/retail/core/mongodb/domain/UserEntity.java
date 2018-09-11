package com.jzfq.retail.core.mongodb.domain;//package com.retail.mongodb.domain;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//import org.springframework.data.mongodb.core.index.CompoundIndex;
//import org.springframework.data.mongodb.core.index.CompoundIndexes;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author lagon
// * @time 2017/2/17 17:57
// * @description 用户实体类
// */
//@Getter
//@Setter
//@NoArgsConstructor
//@ToString
//@Document(collection = "user")
//@CompoundIndexes({
//        @CompoundIndex(name = "birthday_idx", def = "{'name': 1, 'birthday': -1}")
//})
//public class UserEntity extends BaseEntity {
//
//    @Indexed
//    @Field("name")
//    private String name;
//    @Field("birthday")
//    private Date birthday;
//    @Field("addresses")
//    private List<AddressEntity> addresses=new ArrayList<AddressEntity>();
//
//}
