/**
 * 
 */
package com.questionpro.news.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;


/**
 * @author hpst
 *
 */

@UtilityClass
public class NewsUtils {
	
	public <T> CompletableFuture<List<T>> allOf(List<CompletableFuture<T>> futuresList) {
	    CompletableFuture<Void> allFuturesResult =
	    CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[futuresList.size()]));
	    return allFuturesResult.thenApply(v ->
	            futuresList.stream().
	                    map(future -> future.join()).
	                    collect(Collectors.<T>toList())
	    );
	}

}
