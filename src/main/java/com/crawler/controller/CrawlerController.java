package com.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.service.CrawlerService;
import com.crawler.service.MovieDailyService;
import com.crawler.util.DateUtil;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {

	@Autowired
	private CrawlerService crawlerService;

	@Autowired
	private MovieDailyService movieDailyService;

	@RequestMapping(value = "/movie", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void movie(String vailyDate) {
		crawlerService.getMovieTop(vailyDate);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void movieInfo(String id) {
		crawlerService.getMovieInfo(id);
	}

	@RequestMapping(value = "/growthRate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void growthRate(String today) {
		String preday = DateUtil.getPreDate(today);
		while (true) {
			movieDailyService.updateGrowthRate(today, preday);
			today = preday;
			preday = DateUtil.getPreDate(preday);
		}
	}

	@RequestMapping(value = "/growthRateOne", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void growthRateOne(String today) {
		String preday = DateUtil.getPreDate(today);
		movieDailyService.updateGrowthRate(today, preday);

	}
}
