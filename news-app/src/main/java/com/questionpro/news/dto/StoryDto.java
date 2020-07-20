/**
 * 
 */
package com.questionpro.news.dto;

import java.util.List;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoryDto {
	
    private String by;

    private Integer descendants;

    private Integer id;

    private List<Integer> kids;

    private Integer score;

    private Integer time;

    private String title;

    private String type;

    private String url;


}
