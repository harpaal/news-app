/**
 * 
 */
package com.questionpro.news.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author hpst
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopCommentDto {
	@JsonIgnore
	private int totalComments;
	private String text;
	private String by;
	private int age;

}
