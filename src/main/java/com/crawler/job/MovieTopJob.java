package com.crawler.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crawler.model.MovieDaily;
import com.crawler.service.CrawlerService;
import com.crawler.service.MailService;
import com.crawler.service.MovieDailyService;
import com.crawler.service.ValidDateService;
import com.crawler.service.impl.CrawlerServiceImpl;
import com.crawler.util.DateUtil;

@Component
public class MovieTopJob {

	@Autowired
	private CrawlerService crawlerService;

	@Autowired
	private MovieDailyService movieDailyService;

	@Autowired
	private ValidDateService validDateService;

	@Autowired
	private MailService mailService;

	private static final String DEFALUT_DATE = "20170827";

	static List<String> delayNull = new ArrayList<String>();

	private boolean flag = true;

	private static final int TIME = 7200;

	@Scheduled(cron = "0 15 12 * * ?")
	public void delayJob() {
		try {
			Thread.sleep(new Random().nextInt(TIME) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String messages = "新监控时间: " + sdf.format(new Date()) + "\n\n";
		String dateString = DateUtil.getPreDate(new Date());
		String startDay = dateString;
		List<String> msgs = new ArrayList<String>();
		List<String> dateList = new ArrayList<String>();
		while (DEFALUT_DATE.compareTo(dateString) < 0 && (!validDateCheck(dateString))) {// 不存在时，进行抓取
			dateList.add(dateString);
			dateString = DateUtil.getPreDate(dateString);// 前一天
		}
		Collections.reverse(dateList); 
		for (String string : dateList) {
			flag = false;
			int total = crawlerService.getMovieTop(string);
			if (CrawlerServiceImpl.IS_CHANGE) {
				messages = messages + DateUtil.stringFormat(string) + "日数据接口有变化，请注意!\n";
				CrawlerServiceImpl.IS_CHANGE = false;
			}

			if (total > 0) {
				msgs.add("获取到" + DateUtil.stringFormat(string) + "日的网大票房榜单数据共" + total + "条。");
			} else {
				delayNull.add(string);
				msgs.add(DateUtil.stringFormat(string) + "日的网大票房榜单数据暂未提供，将于本日18点后1小时内尝试修复！");
			}
		}

		if (msgs != null && msgs.size() > 0) {
			for (String string : msgs) {
				messages = messages + string + "\n";
			}
		}
		if (flag) {
			messages = messages + "信息完整无需再次获取！\n";
		}

		messages = messages + "\n存在异常信息情况如下：\n";
		int count = 1;
		List<MovieDaily> movieIdNull = movieDailyService.selectMovieIdNull(startDay, dateString);
		if (movieIdNull != null && movieIdNull.size() > 0) {
			messages = messages + count + "、影片ID为空：" + movieIdNull.size() + "条。 \n";
			for (MovieDaily movieDaily : movieIdNull) {
				messages = messages + "《" + movieDaily.getMovieName() + "》\n";
			}
		} else {
			messages = messages + count + "、影片ID为空：0条。 \n";
		}
		count++;
		List<MovieDaily> movieIds = movieDailyService.selectMovieIdsForName();
		if (movieIds != null && movieIds.size() > 0) {
			messages = messages + count + "、影片对应多ID：" + movieIds.size() + "条。\n";
			for (MovieDaily movieDaily : movieIds) {
				String ids = movieDaily.getMovieId().replace(",", "，");

				messages = messages + "《" + movieDaily.getMovieName() + "》 对应的ID有" + movieDaily.getNum() + "个： " + ids
						+ "\n";
			}
		} else {
			messages = messages + count + "、影片对应多ID：0条。\n";
		}
		count++;
		List<MovieDaily> movieNames = movieDailyService.selectMovieNamesForId();
		if (movieNames != null && movieNames.size() > 0) {
			messages = messages + count + "、ID对应多影片：" + movieNames.size() + "条。\n";
			for (MovieDaily movieDaily : movieNames) {
				String[] names = movieDaily.getMovieName().split(",");
				messages = messages + "  ID：" + movieDaily.getMovieId() + " 对应的影片有" + movieDaily.getNum() + "个：";
				for (int i = 0; i < names.length; i++) {
					messages = messages + "《" + names[i] + "》";
					if (i < names.length - 1) {
						messages = messages + "，";
					}
				}
				messages = messages + "\n";
			}
		} else {
			messages = messages + count + "、ID对应多影片：0条。\n";
		}

		messages = messages + "\n提示：异常信息如果存在，第1条将于本日15点后1小时内尝试修复，修复结果由猫眼电影票房信息决定！第2、3条需由人工进行修复！";
		new EmailThread("网大票房排名监控(" + DateUtil.dateToString(new Date()) + ")", messages).run();
	}

	private boolean validDateCheck(String dateString) {
		return validDateService.validDateCheck(dateString);
	}

	class EmailThread implements Runnable {

		private String subject;

		private String content;

		public EmailThread(String subject, String content) {
			this.subject = subject;
			this.content = content;
		}

		@Override
		public void run() {
			mailService.sendSimpleMail(subject, content);
		}

	}
}
