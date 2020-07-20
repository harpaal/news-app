/**
 * 
 */
package com.questionpro.news.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hpst
 *
 */

@Getter
@Setter
@AllArgsConstructor
public class UsersDto {

		private String about;

	    private Integer created;

	    private Integer delay;

	    private String id;

	    private Integer karma;

	    private List<Integer> submitted;
	    
	    
}
