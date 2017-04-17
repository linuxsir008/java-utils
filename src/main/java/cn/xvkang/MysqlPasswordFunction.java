package cn.xvkang;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;

public class MysqlPasswordFunction {
	public static String MySQLPassword(String plainText) throws UnsupportedEncodingException {
		byte[] utf8 = plainText.getBytes("UTF-8");
		return "*" + DigestUtils.shaHex(DigestUtils.sha(utf8)).toUpperCase();
	}
}
