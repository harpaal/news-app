/**
 * 
 */
package com.questionpro.news.service;

import static com.questionpro.news.dto.NewsAppEnums.BEST_STORIES;
import static com.questionpro.news.dto.NewsAppEnums.END_POINT;
import static com.questionpro.news.dto.NewsAppEnums.HN_URL;
import static com.questionpro.news.dto.NewsAppEnums.ITEM;
import static com.questionpro.news.dto.NewsAppEnums.USER;
import static com.questionpro.news.util.NewsUtils.allOf;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.TypeRef;
import com.questionpro.news.dto.CommentsDto;
import com.questionpro.news.dto.StoryDto;
import com.questionpro.news.dto.TopCommentDto;
import com.questionpro.news.dto.UserDto;
import com.questionpro.news.exception.NoPastStoriesException;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@CacheConfig(cacheNames = {"best_stories"})
@Service
public class NewsApiServiceImpl implements NewsApiService{
	
	@Autowired
	private  RestTemplate restTemplate;

    @Autowired
    CacheManager cacheManager;
    
 

	private static Set<StoryDto> pastStoryList = Collections.synchronizedSet(new HashSet<>());

    
    
    @Caching(evict = {
            @CacheEvict(value = "best_stories", allEntries = true)
        })
     public void clearAllCaches() {
            log.info("Cleared all caches");
     }

    
    
 
	

	@Override
	public Set<TopCommentDto> getTopComments(Integer storyId) {
		StoryDto story = getTopRankedStories(storyId);
		SortedSet<TopCommentDto> setOfTopComments = new TreeSet<>();
		try {
			setOfTopComments = getTopCommentsByScore(story);
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getMessage(),e);
		}
		log.info("Done with getting the top comments..");
		return setOfTopComments.stream().limit(10).collect(Collectors.toSet());
	}


	@Override
	public Set<StoryDto> getPastStories() throws NoPastStoriesException {
		if(pastStoryList.isEmpty())
			throw new NoPastStoriesException("No Record found for past stories!!");
		else
			return pastStoryList;
		
	}
	
	   

	public List<StoryDto> getTopStories()  {
		Cache valueWrapper = cacheManager.getCache("best_stories");
		if(valueWrapper.get("topStories")==null) {
			List<Integer> bestStoryIds = getStroryIds(BEST_STORIES.apiValue());
			log.info("best story ids "+bestStoryIds);
			List<StoryDto> listOfBestStories = getTopRankedStoriesByScore(bestStoryIds);
			List<StoryDto> topStories = listOfBestStories.stream().limit(10).collect(Collectors.toList());
			valueWrapper.put("topStories", topStories);
			pastStoryList.addAll(topStories);
			return topStories;
		}else {
			ValueWrapper person = valueWrapper.get("topStories");
			return (List<StoryDto>)person.get();
		}

	}
	
	
	private List<Integer> getStroryIds(String stories) {
		ResponseEntity<List<Integer>> response = restTemplate.exchange(
				HN_URL.apiValue() + stories, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Integer>>() {
				});
		List<Integer> bestStoryIds = response.getBody();
		return bestStoryIds;
	}
	


	private List<StoryDto> getTopRankedStoriesByScore(List<Integer> bestStoryIds) {
		List<StoryDto> listOfBestStories = new ArrayList<StoryDto>();
		List<CompletableFuture<StoryDto>> futureList = bestStoryIds.stream()
				.map(storyId -> CompletableFuture.supplyAsync(() -> getTopRankedStories(storyId)))
				.collect(Collectors.toList());
		CompletableFuture<List<StoryDto>> result = allOf(futureList);
		try {
			listOfBestStories = result.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getMessage(),e);
		}
		listOfBestStories.sort(Comparator.comparing(StoryDto::getScore).reversed());
		return listOfBestStories;
	}

	
	private SortedSet<TopCommentDto> getTopCommentsByScore(StoryDto story)
			throws InterruptedException, ExecutionException {
		SortedSet<TopCommentDto> topCommentSet = new TreeSet<>(Comparator.comparing(TopCommentDto::getTotalComments)
				.reversed().thenComparing(TopCommentDto::getTotalComments));
		if( story.getKids().isEmpty()) {
			return new TreeSet<>();
		}else {
			return getTopComments(story, topCommentSet);
		}
	}




	private SortedSet<TopCommentDto> getTopComments(StoryDto story, SortedSet<TopCommentDto> topCommentSet)
			throws InterruptedException, ExecutionException {
		List<CommentsDto> listOfComments;
		log.info(" Get Top Comments "+story.getKids());
		List<CompletableFuture<CommentsDto>> futureList = 
			     story.getKids()
				.stream()
				.map(commmentsId -> CompletableFuture.supplyAsync(() -> getTopRankedComments(commmentsId)))
				.collect(Collectors.toList());
		CompletableFuture<List<CommentsDto>> result = allOf(futureList);
		listOfComments = result.get();
		listOfComments.stream().forEach(comment->populateTopCommentDtos(topCommentSet, comment));
		return topCommentSet;
	}




	private void populateTopCommentDtos(SortedSet<TopCommentDto> topCommentSet, CommentsDto comment) {
		if(comment.getKids()!=null) {
			int commentCount=comment.getKids().size();
			TopCommentDto topComment = new TopCommentDto(commentCount,comment.getText(), comment.getBy(),
					getUserAgeByName(comment.getBy()));
			topCommentSet.add(topComment);
		}
	}
	
	
	
	
	

	private int getUserAgeByName(String by) {
		ResponseEntity<UserDto> response = restTemplate.exchange(
				HN_URL.apiValue()+USER.apiValue() +by+ END_POINT.apiValue(), HttpMethod.GET, null,UserDto.class);
		UserDto user = response.getBody();
		return Period.between(
				Instant.ofEpochMilli(user.getCreated()).atZone(ZoneId.systemDefault()).toLocalDate(),
				LocalDate.now()).getYears();

	}


	private StoryDto getTopRankedStories(Integer storyId) {
		ResponseEntity<StoryDto> storyResponse = restTemplate.exchange(
				HN_URL.apiValue() + ITEM.apiValue() + storyId + END_POINT.apiValue(),
				HttpMethod.GET, null, StoryDto.class);
		return storyResponse.getBody();
	}
	
	private CommentsDto getTopRankedComments(Integer commentId) {
		ResponseEntity<CommentsDto> commentsResponse = restTemplate.exchange(
				HN_URL.apiValue() + ITEM.apiValue() + commentId + END_POINT.apiValue(),
				HttpMethod.GET, null, CommentsDto.class);
		return commentsResponse.getBody();
	}


	


}
