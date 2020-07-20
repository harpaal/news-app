/**
 * 
 */
package com.questionpro.news.service;

import java.util.List;
import java.util.Set;

import com.questionpro.news.dto.StoryDto;
import com.questionpro.news.dto.TopCommentDto;
import com.questionpro.news.exception.NoPastStoriesException;



public interface NewsApiService {

	public List<StoryDto> getTopStories() ;

	public Set<TopCommentDto> getTopComments(Integer storyId);

	public void clearAllCaches();

	public  Set<StoryDto> getPastStories() throws NoPastStoriesException;
}
