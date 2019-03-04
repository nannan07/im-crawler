package com.crawler.model;

import com.crawler.util.UUIDUtil;

public class MovieType {
	private String id;

	private String movieId;

	private String typeId;

	private String typeName;

	private Integer orderNum;

	public MovieType() {

	}

	public MovieType(String movieId, String typeName, int orderNum) {
		this.id = UUIDUtil.getUUID();
		this.movieId = movieId;
		this.typeName = typeName;
		this.orderNum = orderNum;
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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId == null ? null : typeId.trim();
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName == null ? null : typeName.trim();
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
}