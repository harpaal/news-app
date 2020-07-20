/**
 * 
 */
package com.questionpro.news.exception;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author hpst
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler 
  extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler(value 
      = { NoPastStoriesException.class })
    protected ResponseEntity<Object> handleNoStory(
    		RuntimeException ex, WebRequest request) {
    	String bodyOfResponse = "There are no past stories avaiable , please try again after some time";
    	return handleExceptionInternal(ex, bodyOfResponse, 
    			new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }
    
    @ExceptionHandler(value 
    	      = { IOException.class })
    	    protected ResponseEntity<Object> handleNetworkExp(
    	    		RuntimeException ex, WebRequest request) {
    	    	String bodyOfResponse = "Looks like Network is down,  please try after some time";
    	    	return handleExceptionInternal(ex, bodyOfResponse, 
    	    			new HttpHeaders(), HttpStatus.FAILED_DEPENDENCY, request);
    	    }
}