package com.crawler.service;

import java.util.List;

import com.crawler.model.MovieDaily;

public interface CrawlerService {
	int getMovieTop(String dateString);
	
	List<MovieDaily> getMovieDailyList(String dateString);

	int updateMovieDailyList(List<MovieDaily> md);
	
	void getMovieInfo(String movieId);
}
