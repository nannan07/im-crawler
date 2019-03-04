package com.crawler.model;

import java.math.BigDecimal;
import java.util.Date;

import com.crawler.model.vo.MovieDailyJson;
import com.crawler.util.DateUtil;
import com.crawler.util.UUIDUtil;

public class MovieDaily {
	private String id;

	private String movieId;

	private String movieName;

	private String dailyDate;

	private String releaseDate;

	private Short releaseDays;

	private BigDecimal dailyBox;

	private BigDecimal sumBox;
	
	private BigDecimal weeklyBox;

	private Byte topOrder;

	private Date createdAt;

	private Byte num;
	
	private String growthRate;
	
	public MovieDaily() {

	}

	public MovieDaily(MovieDailyJson movieDailyJson, String dateString, Byte topOrder) {
		if (movieDailyJson != null) {
			this.id = UUIDUtil.getUUID();
			this.movieId = movieDailyJson.getMovieId();
			this.movieName = movieDailyJson.getMovieName();
			this.dailyBox = new BigDecimal(movieDailyJson.getDailyBox());
			this.releaseDays = movieDailyJson.getReleaseDays();
			String sbox = movieDailyJson.getSumBox();
			this.sumBox = new BigDecimal(sbox.substring(0, sbox.length() - 1));
			String wBox = movieDailyJson.getWeeklyBox();
			if(wBox.equals("--")) {
				this.weeklyBox = new BigDecimal(0);
			}else {
				this.weeklyBox = new BigDecimal(wBox.substring(0, wBox.length() - 1));
			}
			this.dailyDate = dateString;
			this.topOrder = topOrder;
			this.releaseDate = DateUtil.getPreDates(dailyDate, releaseDays);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId == null ? null : movieId.trim();
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName == null ? null : movieName.trim();
	}

	public String getDailyDate() {
		return dailyDate;
	}

	public void setDailyDate(String dailyDate) {
		this.dailyDate = dailyDate == null ? null : dailyDate.trim();
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate == null ? null : releaseDate.trim();
	}

	public Short getReleaseDays() {
		return releaseDays;
	}

	public void setReleaseDays(Short releaseDays) {
		this.releaseDays = releaseDays;
	}

	public BigDecimal getDailyBox() {
		return dailyBox;
	}

	public void setDailyBox(BigDecimal dailyBox) {
		this.dailyBox = dailyBox;
	}

	public BigDecimal getSumBox() {
		return sumBox;
	}

	public void setSumBox(BigDecimal sumBox) {
		this.sumBox = sumBox;
	}

	public BigDecimal getWeeklyBox() {
		return weeklyBox;
	}

	public void setWeeklyBox(BigDecimal weeklyBox) {
		this.weeklyBox = weeklyBox;
	}

	public Byte getTopOrder() {
		return topOrder;
	}

	public void setTopOrder(Byte topOrder) {
		this.topOrder = topOrder;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Byte getNum() {
		return num;
	}

	public void setNum(Byte num) {
		this.num = num;
	}

	public String getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(String growthRate) {
		this.growthRate = growthRate;
	}
}