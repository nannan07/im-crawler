package com.crawler.model.vo;

import com.crawler.model.ValidDates;

public class ValidDateVo {
	private String date;

	private Boolean flag;

	public ValidDateVo() {

	}

	public ValidDateVo(ValidDates validDates) {
		if(validDates != null) {
			this.date = validDates.getDate();
			this.flag = validDates.getFlag();
		}
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
}
