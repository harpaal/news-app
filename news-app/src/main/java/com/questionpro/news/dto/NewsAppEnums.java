package com.questionpro.news.dto;

public enum NewsAppEnums {
	
	HN_URL("https://hacker-news.firebaseio.com/v0/"),
	TOP_STORIES("/topstories.json?print=pretty"),
	BEST_STORIES("/beststories.json?print=pretty"),
	END_POINT(".json?print=pretty"),
	ITEM("/item/"),
	USER("/user/");
	
	private String apiValue;
	
	NewsAppEnums(String value){
		this.apiValue= value;
	}
	
	public String apiValue() {
		return apiValue;
	}
	
}
