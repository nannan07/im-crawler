package com.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.service.ValidDateService;
import com.crawler.util.StrUtil;

@RestController
@RequestMapping("/validDate")
public class ValidDateController {
	
	@Autowired
	private ValidDateService validDateService;
	
	@RequestMapping(value = "/select", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object selectValidDate(String startDate) {
		if(StrUtil.isEmpty(startDate)) {
			return null;
		}
		return validDateService.selectValidDate(startDate);

	}
}
