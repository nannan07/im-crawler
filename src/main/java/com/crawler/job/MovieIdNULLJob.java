package com.crawler.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crawler.model.MovieDaily;
import com.crawler.service.CrawlerService;
import com.crawler.service.MovieDailyService;
import com.crawler.util.StrUtil;

@Component
public class MovieIdNULLJob {
	
	private static final int TIME = 3600;
	
	@Autowired
	private MovieDailyService movieDailyService;
	
	@Autowired
	private CrawlerService crawlerService;
	
	@Scheduled(cron = "0 15 15 * * ?")
	public void dealMovieIdNULLJob() {
		try {
			Thread.sleep(new Random().nextInt(TIME) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<MovieDaily> list = movieDailyService.getMovieIdNULL();
		Map<String, List<MovieDaily>> map = new HashMap<String, List<MovieDaily>>();
		List<String> dailyDates = new ArrayList<String>();
		for (MovieDaily movieDaily : list) {
			String dailyDate = movieDaily.getDailyDate();
			dailyDates.add(dailyDate);// 没有moviedId的日期
			List<MovieDaily> mdList = new ArrayList<MovieDaily>();
			if (map.containsKey(dailyDate)) {
				mdList = map.get(dailyDate);
			}
			mdList.add(movieDaily);
			map.put(dailyDate, mdList);// 同一天内的没有movieId的集合
		}

		List<MovieDaily> md = new ArrayList<MovieDaily>();
		List<String> movieIds = new ArrayList<String>();
		for (String string : dailyDates) {
			List<MovieDaily> newList = crawlerService.getMovieDailyList(string);
			Map<Object, String> map1 = new HashMap<Object, String>();
			for (MovieDaily movieDaily : newList) {
				map1.put(movieDaily.getTopOrder(), movieDaily.getMovieId());
			}
			List<MovieDaily> oldList = map.get(string);
			for (MovieDaily movieDaily : oldList) {
				movieDaily.setMovieId(map1.get(movieDaily.getTopOrder()));
				if (StrUtil.isEmpty(movieDaily.getMovieId())) {
					continue;
				}
				movieIds.add(movieDaily.getMovieId());
				md.add(movieDaily);
			}

		}
		if (md != null && md.size() > 0) {
			crawlerService.updateMovieDailyList(md);
		}
		for (String string : movieIds) {
			crawlerService.getMovieInfo(string);
		}
	}
}
