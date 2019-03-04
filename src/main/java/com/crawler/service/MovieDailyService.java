package com.crawler.service;

import java.util.List;

import com.crawler.model.MovieDaily;

public interface MovieDailyService {

	List<MovieDaily> getMovieIdNULL();

	List<MovieDaily> selectMovieIdNull(String startDay, String stopDay);

	List<MovieDaily> selectMovieIdsForName();

	List<MovieDaily> selectMovieNamesForId();

	int updateGrowthRate(String today, String preday);

	List<MovieDaily> selectMovie(String today);
	
}
