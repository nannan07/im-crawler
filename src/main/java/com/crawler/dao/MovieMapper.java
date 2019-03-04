package com.crawler.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.crawler.model.Movie;

@Component
@Mapper
public interface MovieMapper {
	
    int insertSelective(Movie record);

	int checkMovieId(String movieId);
}