/**
 * 
 */
package com.questionpro.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hpst
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private String about;
	private int created;
	private int delay;
	private String id;
	private int karma;
	private int[] submitted;
}
