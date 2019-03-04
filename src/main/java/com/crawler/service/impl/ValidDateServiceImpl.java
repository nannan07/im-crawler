package com.crawler.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crawler.dao.ValidDatesMapper;
import com.crawler.model.ValidDates;
import com.crawler.service.ValidDateService;
import com.crawler.util.StrUtil;
import com.crawler.util.UUIDUtil;

@Service
public class ValidDateServiceImpl implements ValidDateService {

	@Autowired
	private ValidDatesMapper validDateDao;

	@Override
	public Map<String, String> selectValidDate(String startDate) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("startDate", startDate + "01");
		map.put("stopDate", startDate + "31");
		List<ValidDates> vdList = validDateDao.selectValidDate(map);
		Map<String, String> param = new HashMap<String, String>();
		for (ValidDates validDates : vdList) {
			String flag = "0";
			if (validDates.getFlag() == true) {
				flag = "1";
			}
			param.put(validDates.getDate(), flag);
		}
		return param;
	}

	@Override
	public boolean validDateCheck(String dateString) {
		int count = validDateDao.validDateCheck(dateString);
		return (count == 0) ? false : true;
	}

	@Override
	public boolean updateValidDate(String dateString) {
		validDateDao.updateByPrimaryKeySelective(dateString);
		return true;
	}

	@Override
	public boolean updateValidDateToo(String dateString) {
		String id = validDateDao.dateCheck(dateString);
		if (StrUtil.isEmpty(id)) {// 不存在
			ValidDates record = new ValidDates();
			record.setDate(dateString);
			String uuId = UUIDUtil.getUUID();
			record.setId(uuId);
			validDateDao.insertSelective(record); // 添加
		}
		return true;
	}

}
