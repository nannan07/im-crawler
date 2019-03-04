package com.crawler.model;

import java.util.Date;

import com.crawler.util.UUIDUtil;

public class Movie {
	private String id;

	private String movieId;

	private String movieName;

	private String releaseDate;

	private Integer duration;

	private Date createdAt;

	public Movie() {

	}

	public Movie(String movieId, String movieName, String releaseDate, Integer duration) {
		this.id = UUIDUtil.getUUID();
		this.movieId = movieId;
		this.movieName = movieName;
		this.releaseDate = releaseDate;
		this.duration = duration;
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

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate == null ? null : releaseDate.trim();
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}