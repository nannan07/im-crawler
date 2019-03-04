package com.crawler.model.vo;

public class MovieDailyJson {

    private String movieId;

    private String movieName;

    private Short releaseDays;

    private String dailyBox;

    private String sumBox;
    
    private String weeklyBox;
    
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

    public Short getReleaseDays() {
        return releaseDays;
    }

    public void setReleaseDays(Short releaseDays) {
        this.releaseDays = releaseDays;
    }

    public String getDailyBox() {
        return dailyBox;
    }

    public void setDailyBox(String dailyBox) {
        this.dailyBox = dailyBox;
    }
    public String getSumBox() {
		return sumBox;
	}

	public void setSumBox(String sumBox) {
		this.sumBox = sumBox;
	}

	public String getWeeklyBox() {
		return weeklyBox;
	}

	public void setWeeklyBox(String weeklyBox) {
		this.weeklyBox = weeklyBox;
	}
}