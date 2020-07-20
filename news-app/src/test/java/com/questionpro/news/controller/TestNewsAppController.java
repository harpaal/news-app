/**
 * 
 */
package com.questionpro.news.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.questionpro.news.service.NewsApiService;

/**
 * @author hpst
 *
 */

@RunWith(SpringRunner.class)
@WebMvcTest(NewsAppController.class)
public class TestNewsAppController {
	
	
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
    private NewsApiService service;
	
	@Test
	public void testBestStories() throws Exception {
		mockMvc.perform( MockMvcRequestBuilders
				.get("/best-stories")
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testPastStories() throws Exception {
		mockMvc.perform( MockMvcRequestBuilders
				.get("/past-stories/")
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	
	@Test
	public void testTopComments() throws Exception {
		String storyId= "23886047";
		mockMvc.perform( MockMvcRequestBuilders
				.get("/comments/"+storyId)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	
}
	
	