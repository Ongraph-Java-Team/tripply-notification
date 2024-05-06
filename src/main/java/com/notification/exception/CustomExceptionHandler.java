package com.notification.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

	 @ExceptionHandler(BadRequestException.class)
	    public ResponseEntity<Object> handleCustomException(BadRequestException ex) {
	        CustomErrorResponse errorResponse = new CustomErrorResponse();
	        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	        errorResponse.setMessage("Hotel Already Invited"); 
	        logger.error("BadRequest Exception Occured:-{} ", errorResponse.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	    }

	 @ExceptionHandler(DataNotFoundException.class)
	    public ResponseEntity<Object> handleCustomException1(DataNotFoundException ex) {
	        CustomErrorResponse errorResponse = new CustomErrorResponse();
	        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
	        errorResponse.setMessage("Invitation not Found"); 
	        logger.error("DataNotFound Exception Occured:-{} ", errorResponse.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }
}
