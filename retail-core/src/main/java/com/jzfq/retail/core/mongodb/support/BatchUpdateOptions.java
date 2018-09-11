package com.jzfq.retail.core.mongodb.support;//package com.retail.mongodb.support;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
//@Getter
//@Setter
//@ToString
//public class BatchUpdateOptions {
//
//	private Query query;
//	private Update update;
//	private boolean upsert = false;
//	private boolean multi = false;
//
//	public BatchUpdateOptions() {
//	}
//
//	public BatchUpdateOptions(Query query, Update update) {
//		this.query = query;
//		this.update = update;
//	}
//
//	public BatchUpdateOptions(Query query, Update update, boolean upsert, boolean multi) {
//		this.query = query;
//		this.update = update;
//		this.upsert = upsert;
//		this.multi = multi;
//	}
//
//}
