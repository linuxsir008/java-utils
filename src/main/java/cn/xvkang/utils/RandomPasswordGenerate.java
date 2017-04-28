package cn.xvkang.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomPasswordGenerate {
	public static String generate(){
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
		String pwd = RandomStringUtils.random(6, characters);
		//System.out.println(pwd);
		return pwd;
	}
}
