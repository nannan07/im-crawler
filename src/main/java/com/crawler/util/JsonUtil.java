package com.crawler.util;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

	public static List<String> getAllKeys(String jsonString) {
		 List<String> list = new ArrayList<String>();
		// 按照","将json字符串分割成String数组
		String[] keyValue = jsonString.split(",");
		for (int i = 0; i < keyValue.length; i++) {
			String s = keyValue[i];
			// 找到":"所在的位置，然后截取
			int index = s.indexOf(":");
			// 第一个字符串因带有json的"{"，需要特殊处理
			if (i == 0) {
				list.add(s.substring(2, index - 1));
			} else {
				list.add(s.substring(1, index - 1));
			}
		}
		return list;
	}
}
