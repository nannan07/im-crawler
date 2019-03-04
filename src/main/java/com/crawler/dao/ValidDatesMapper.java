package com.crawler.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.crawler.model.ValidDates;

@Component
@Mapper
public interface ValidDatesMapper {
	
    List<ValidDates> selectValidDate(Map<String, String> map);

	int insertSelective(ValidDates record);
    
    int updateByPrimaryKeySelective(String dateString);
    
	int validDateCheck(String dateString);

	String dateCheck(String dateString);
}