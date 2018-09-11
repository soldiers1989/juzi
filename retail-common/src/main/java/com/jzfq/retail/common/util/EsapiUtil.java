package com.jzfq.retail.common.util;

//import org.owasp.esapi.ESAPI;
//import org.owasp.esapi.codecs.MySQLCodec;
//import org.owasp.esapi.codecs.MySQLCodec.Mode;

/**
 * SQL注入攻击防护
 *
 * @author liu wei
 * @create 2017-09-08 18:01
 * @since v1.0
 */
public class EsapiUtil {

	/**
	 * 这个可以简单防护，但还不够，只是抛砖引玉
	 *
	 * @param str
	 * @return
	 */
	public static String sql(String str) {
//		MySQLCodec codec = new MySQLCodec(Mode.ANSI);
//		return ESAPI.encoder().encodeForSQL(codec, str);
		return null;
	}

}
