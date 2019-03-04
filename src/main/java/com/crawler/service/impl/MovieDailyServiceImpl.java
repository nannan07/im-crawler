package com.crawler.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crawler.dao.MovieDailyMapper;
import com.crawler.model.MovieDaily;
import com.crawler.service.MovieDailyService;

@Service
public class MovieDailyServiceImpl implements MovieDailyService {
	
	@Autowired
	private MovieDailyMapper movieDailyDao;

	@Override
	public List<MovieDaily> getMovieIdNULL() {
		return movieDailyDao.getMovieIdNULL();
	}

	@Override
	public List<MovieDaily> selectMovieIdNull(String startDay, String stopDay) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("startDay", startDay);
		map.put("stopDay", stopDay);
		return movieDailyDao.selectMovieIdNull(map);
	}

	@Override
	public List<MovieDaily> selectMovieIdsForName() {
		return movieDailyDao.selectMovieIdsForName();
	}

	@Override
	public List<MovieDaily> selectMovieNamesForId() {
		return movieDailyDao.selectMovieNamesForId();
	}

	@Override
	public int updateGrowthRate(String today, String preday) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("toDay", today);
		map.put("preDay", preday);
		return movieDailyDao.updateGrowthRate(map);
	}

	@Override
	public List<MovieDaily> selectMovie(String today) {
		return movieDailyDao.selectMovieByCreateDate(today);
	}


	
}
