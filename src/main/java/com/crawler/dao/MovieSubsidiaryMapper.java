package com.crawler.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.crawler.model.MovieSubsidiary;

@Component
@Mapper
public interface MovieSubsidiaryMapper {

	int insertSelective(MovieSubsidiary record);

}