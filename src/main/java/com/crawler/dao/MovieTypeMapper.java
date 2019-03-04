package com.crawler.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.crawler.model.MovieType;

@Component
@Mapper
public interface MovieTypeMapper {

	int insertList(List<MovieType> movieTypeList);
}