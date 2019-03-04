package com.crawler.model;

import com.crawler.util.UUIDUtil;

public class MovieSubsidiary {
	private String id;

	private String movieId;

	private String summary;

	public MovieSubsidiary() {

	}

	public MovieSubsidiary(String movieId, String summary) {
		this.id = UUIDUtil.getUUID();
		this.movieId = movieId;
		this.summary = summary;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary == null ? null : summary.trim();
	}
}