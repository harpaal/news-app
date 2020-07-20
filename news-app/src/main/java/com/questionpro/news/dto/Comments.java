package com.questionpro.news.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comments {
	
	private String by;

    private Integer id;

    private List<Integer> kids;

    private Integer parent;

    private String text;

    private Integer time;

    private String type;

}
