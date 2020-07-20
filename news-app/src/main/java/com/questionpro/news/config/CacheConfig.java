package com.questionpro.news.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;


@Configuration
public class CacheConfig {

	
	@Bean
	public CacheManager cacheManager() {
	    CaffeineCacheManager cacheManager = new CaffeineCacheManager("best_stories");
	    cacheManager.setAllowNullValues(false);
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }



    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterAccess(15, TimeUnit.MINUTES)
                .weakKeys()
                .removalListener(new CustomRemovalListener())
                .recordStats();
    }

    class CustomRemovalListener implements RemovalListener<Object, Object>{
        @Override
        public void onRemoval(Object key, Object value, RemovalCause cause) {
            System.out.format("removal listerner called with key [%s], cause [%s], evicted [%S]\n", 
                    key, cause.toString(), cause.wasEvicted());
        }
    }

}