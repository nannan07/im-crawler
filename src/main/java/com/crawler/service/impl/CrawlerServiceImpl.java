package com.crawler.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crawler.dao.MovieDailyMapper;
import com.crawler.dao.MovieMapper;
import com.crawler.dao.MoviePersonMapper;
import com.crawler.dao.MovieSubsidiaryMapper;
import com.crawler.dao.MovieTypeMapper;
import com.crawler.model.Movie;
import com.crawler.model.MovieDaily;
import com.crawler.model.MoviePerson;
import com.crawler.model.MovieSubsidiary;
import com.crawler.model.MovieType;
import com.crawler.model.vo.MovieDailyJson;
import com.crawler.service.CrawlerService;
import com.crawler.service.ValidDateService;
import com.crawler.util.DateUtil;
import com.crawler.util.JsonUtil;
import com.crawler.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class CrawlerServiceImpl implements CrawlerService {

	private static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19";

	private static final String MOVIE_TOP_URL = "https://box.maoyan.com/proseries/api/netmovie/boxRank.json?date=";

	private static final String MOVIE_INFO_URL = "https://piaofang.maoyan.com/netMovie/";

	private static final int TIME = 60;

	public static boolean IS_CHANGE = false;

	private static final List<String> DEFAULT_LIST = Arrays.asList("sumBox", "dailyBoxV2", "weeklyBox", "dailyBox",
			"movieId", "movieName", "releaseDays");

	@Autowired
	private ValidDateService validDateService;

	@Autowired
	private MovieDailyMapper movieDailyDao;

	@Autowired
	private MovieMapper movieDao;

	@Autowired
	private MovieTypeMapper movieTypeDao;

	@Autowired
	private MovieSubsidiaryMapper movieSubsidiaryDao;

	@Autowired
	private MoviePersonMapper moviePersonDao;

	/**
	 * 获取当日的影片排行
	 */
	public int getMovieTop(String dateString) {
		
		validDateService.updateValidDateToo(dateString);// 有效日期置为0
		List<MovieDaily> list = getMovieDailyList(dateString);
		for (MovieDaily movieDaily : list) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("preday", DateUtil.getPreDate(dateString));
			map.put("movieName", movieDaily.getMovieName());
			MovieDaily md = movieDailyDao.selectPreDailyBox(map);
			if (md != null) {
				BigDecimal dailyBox = md.getDailyBox();
				BigDecimal dailyNew = movieDaily.getDailyBox();
				BigDecimal growthRate = dailyNew.subtract(dailyBox);
				growthRate = growthRate.divide(dailyBox, 4, RoundingMode.HALF_UP);
				growthRate = growthRate.multiply(new BigDecimal(100));
				movieDaily.setGrowthRate(growthRate.setScale(2).toString()+"%");
			}
		}
		if (list != null && list.size() > 0) {
			movieDailyDao.insertMovieList(list);// 存储当日上榜影片信息
			validDateService.updateValidDate(dateString);// 有效日期置为1
		}
		List<String> movieIds = new ArrayList<String>();
		for (MovieDaily movieDaily : list) {
			String movieId = movieDaily.getMovieId();
			if (StrUtil.notEmpty(movieId)) {
				movieIds.add(movieId);
			}
		}
		// 二级抓取
		for (String string : movieIds) {
			if (StrUtil.isEmpty(string)) {
				continue;
			}
			new CrawlerThread(string).start();
		}
		return list.size();
	}

	public List<MovieDaily> getMovieDailyList(String dateString) {
		List<MovieDaily> list = new ArrayList<MovieDaily>();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(MOVIE_TOP_URL + dateString);
		httpGet.setHeader("user-agent", USER_AGENT);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();// 调用getEntity()方法获取到一个HttpEntity实例
			if (entity != null) {
				String json = null;
				try {
					json = EntityUtils.toString(entity, "UTF-8");
				} catch (ParseException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
				JsonArray jsonArray = jsonObject.getAsJsonArray("data");
				// 循环遍历数组
				Byte count = 1;
				for (JsonElement e : jsonArray) {
					isChange(e);
					MovieDailyJson movieDailyJson = new Gson().fromJson(e, MovieDailyJson.class);
					list.add(new MovieDaily(movieDailyJson, dateString, count));
					count = (byte) (count + 1);
				}
			}
		}
		return list;
	}

	@Override
	public int updateMovieDailyList(List<MovieDaily> md) {
		return movieDailyDao.updateByPrimaryKeySelective(md);
	}

	class CrawlerThread extends Thread {
		String movieId;

		public CrawlerThread(String movieId) {
			this.movieId = movieId;
		}

		public void run() {
			try {
				Thread.sleep(new Random().nextInt(TIME) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getMovieInfo(movieId);
		}
	}

	public void getMovieInfo(String movieId) {
		int count = movieDao.checkMovieId(movieId);
		if (count == 0) {// 是否存在movie? 存在不需要再次抓取，不存在进行抓取
			Connection con = Jsoup.connect(MOVIE_INFO_URL + movieId);
			con.header("user-agent", USER_AGENT);
			Document doc = null;
			try {
				doc = con.get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements details = doc.getElementsByClass("info-detail");
			if (null != details && details.size() > 0) {
				Element detail = details.first();
				String movieName = null;
				String duration = "0";
				String releaseDate = null;
				Element movieNameEs = detail.getElementsByClass("info-title ellipsis-1").first();
				if (movieNameEs != null) {
					movieName = movieNameEs.text();
				}
				Element durationEs = detail.getElementsByClass("info-duration ellipsis-1").first();
				if (durationEs != null) {
					duration = durationEs.text().replace(Jsoup.parse("&nbsp;").text(), " ").trim();
					duration = duration.replaceAll(" ", "");
					if (StrUtil.notEmpty(duration)) {
						duration = duration.substring(0, duration.length() - 2);
					} else {
						duration = "0";
					}
				}
				Element releaseDateEs = detail.getElementsByClass("info-release ellipsis-1").first();
				if (releaseDateEs != null) {
					releaseDate = releaseDateEs.text();
					String regEx = "[^0-9]";
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(releaseDate);
					releaseDate = m.replaceAll("").trim();
				}
				Movie movie = new Movie(movieId, movieName, releaseDate, Integer.valueOf(duration));
				movieDao.insertSelective(movie);
				Elements typeEs = detail.getElementsByClass("info-subtype ellipsis-1");
				if (typeEs != null && typeEs.size() > 0) {
					Element es = typeEs.first();
					if (es != null) {
						List<MovieType> movieTypeList = new ArrayList<MovieType>();
						String type = es.text();
						List<String> types = Arrays.asList(type.split("/"));
						int orderNum = 1;
						for (String string : types) {
							movieTypeList.add(new MovieType(movieId, string.trim(), orderNum));
							orderNum = orderNum + 1;
						}
						if (movieTypeList != null && movieTypeList.size() > 0) {
							movieTypeDao.insertList(movieTypeList);
						}
					}
				}
			}

			Element summaryEs = doc.getElementsByClass("detail-block-content").first();
			if (summaryEs != null) {
				String summary = summaryEs.text();
				MovieSubsidiary movieSubsidiary = new MovieSubsidiary(movieId, summary);
				movieSubsidiaryDao.insertSelective(movieSubsidiary);
			}
			List<MoviePerson> moviePersonList = new ArrayList<MoviePerson>();
			Elements category = doc.getElementsByClass("category");
			for (Element element : category) {
				String typeName = element.getElementsByClass("need-sticky cat-title ellipsis-1").text();
				Element personItem = element.getElementsByClass("items").first();
				Elements item = personItem.getElementsByClass("item");
				int orderN = 1;
				for (Element element2 : item) {
					String namesP = element2.getElementsByClass("title ellipsis-1").text();
					String roleNamesP = element2.getElementsByClass("info ellipsis-1").text();
					if (StrUtil.notEmpty(roleNamesP.trim())) {
						roleNamesP = roleNamesP.substring(2);
					}
					moviePersonList.add(new MoviePerson(movieId, namesP, roleNamesP, typeName, orderN));
					orderN = orderN + 1;
				}
			}
			if (moviePersonList != null && moviePersonList.size() > 0) {
				moviePersonDao.insertList(moviePersonList);
			}
		}
	}

	private void isChange(JsonElement e) {
		if (!IS_CHANGE) {
			List<String> list = JsonUtil.getAllKeys(e.toString());
			for (String string : list) {
				if (!DEFAULT_LIST.contains(string)) {
					IS_CHANGE = true;
					break;
				}
			}
		}
	}

}
