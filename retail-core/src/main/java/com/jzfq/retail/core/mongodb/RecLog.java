package com.jzfq.retail.core.mongodb;

/**
 * 标志性接口
 */
public interface RecLog {

    /**
     * 1.主键，不可重复，自带索引，可以在定义的列名上标注，需要自己生成并维护不重复的约束。如果自己不设置@Id主键，mongo会自动生成一个唯一主键，并且插入时效率远高于自己设置主键。
     * @Id
     *
     * 2.声明该字段需要索引，建索引可以大大的提高查询效率。为某个字段申请一个索引。datastore.ensureIndexes() 方法被调用时 这些索引就会被申请.
     * @Indexed(unique = true) 唯一索引
     *
     * 参数说明如下：value: 表名这个索引的方向； IndexDirection.ASC(升序)，IndexDirection.DESC(降序), IndexDirection.BOTH(两者)默认为 升序；
     *             name： 被创建的索引的 名称； mongodb默认创建的索引名的格式为(key1_1/-1_key2_1);
     *             unique： 创建一个唯一索引，当创建唯一索引后，当在此字段插入相同的值时将会报错。true:为唯一索引；false：不是唯一索引。默认为：false;
     *             dropDups：此参数表明，当为某个字段创建唯一索引时，删除其他相同值的记录。只保留第一条记录。true:删除重复，false:不删除重复（当有重复值时唯一索引创建失败）；默认为false.
     * @Indexed(value=IndexDirection.ASC, name="upc", unique=true, dropDups=true)
     *
     * 3.复合索引的声明，建复合索引可以有效地提高多字段的查询效率(def里两参数将作为复合索引，数字参数指定索引的方向，1为正序，-1为倒序,方向对单键索引和随机存不要紧，但如果你要执行分组和排序操作的时候，它就非常重要了)。
     * @CompoundIndexes({@CompoundIndex(name = "logManual_index", def = "{biz_id : 1, id : 1}")})
     *
     * 4.被该注解标注的，将不会被录入到数据库中。只作为普通的javaBean属性。
     * @Transient
     *
     * 5.关联另一个document对象。类似于mysql的表关联，但并不一样，mongo不会做级联的操作。
     * @DBRef
     *
     *
     */

}
