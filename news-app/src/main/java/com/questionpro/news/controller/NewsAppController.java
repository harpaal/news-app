/**
 * 
 */
package com.questionpro.news.controller;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.questionpro.news.dto.StoryDto;
import com.questionpro.news.dto.TopCommentDto;
import com.questionpro.news.exception.NoPastStoriesException;
import com.questionpro.news.service.NewsApiService;

import lombok.extern.slf4j.Slf4j;


/**
 * @author hpst
 *
 */

@RestController
@Slf4j
public class NewsAppController {

	@Autowired
	private NewsApiService service;
		
	@GetMapping("/best-stories")
	@ResponseBody
	public  List<StoryDto> getBestStories() throws JsonProcessingException  {
		log.info("Going to get the best stories..");
		List<StoryDto> topStories = service.getTopStories();
		log.info("Done with getting the best stories..");
		return topStories;
	}

	

	@GetMapping("/past-stories")
	public  Set<StoryDto> getPastStories() throws NoPastStoriesException {
		log.info("Going to get the past stories..");
		Set<StoryDto> pastStories = service.getPastStories();
		log.info("Done getting past stories..");
		return pastStories;
	}
	
	
	@GetMapping("/clear-chache")
	public void clearAllCaches() {
		service.clearAllCaches();
	}

	
	@GetMapping("/comments/{story-id}")
	@ResponseBody
	public Set<TopCommentDto> getTopComments(@PathVariable("story-id") Integer storyId ) {
		log.info("Going to get top comments for story "+storyId);
		Set<TopCommentDto> topComments = service.getTopComments(storyId);
		log.info("Done getting top comments for story "+storyId);
		return topComments;
	}
	
	
}
