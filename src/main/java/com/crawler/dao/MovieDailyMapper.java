package com.crawler.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.crawler.model.MovieDaily;

@Component
@Mapper
public interface MovieDailyMapper {

	int updateByPrimaryKeySelective(List<MovieDaily> md);

	int insertMovieList(List<MovieDaily> list);

	List<MovieDaily> getMovieIdNULL();

	List<MovieDaily> selectMovieIdNull(Map<String, String> map);

	List<MovieDaily> selectMovieIdsForName();

	List<MovieDaily> selectMovieNamesForId();

	MovieDaily selectPreDailyBox(Map<String, Object> map);

	int updateGrowthRate(Map<String, String> map);

	List<MovieDaily> selectMovieByCreateDate(String today);
	
}