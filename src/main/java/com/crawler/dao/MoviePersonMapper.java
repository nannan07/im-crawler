package com.crawler.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.crawler.model.MoviePerson;

@Component
@Mapper
public interface MoviePersonMapper {
	
	int insertList(List<MoviePerson> moviePersonList);
}