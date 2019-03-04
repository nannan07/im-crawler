package com.crawler.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crawler.service.CrawlerService;
import com.crawler.service.ValidDateService;

@Component
public class DelayNullJob {

	private static final int TIME = 3600;

	@Autowired
	private CrawlerService crawlerService;

	@Autowired
	private ValidDateService validDateService;

	@Scheduled(cron = "0 0 18 * * ?")
	public void delayNullJob() {
		List<String> delayNull = new ArrayList<String>();
		delayNull.addAll(MovieTopJob.delayNull);
		if (delayNull != null && delayNull.size() > 0) {
			try {
				Thread.sleep(new Random().nextInt(TIME) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (String string : delayNull) {
				if (validDateService.validDateCheck(string)) {
					continue;
				}
				crawlerService.getMovieTop(string);
			}

		}
		delayNull.clear();
	}

}
