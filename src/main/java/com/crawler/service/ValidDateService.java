package com.crawler.service;

import java.util.Map;

public interface ValidDateService {

	boolean validDateCheck(String dateString);

	boolean updateValidDate(String dateString);

	Map<String, String> selectValidDate(String startDate);

	boolean updateValidDateToo(String dateString);
}
