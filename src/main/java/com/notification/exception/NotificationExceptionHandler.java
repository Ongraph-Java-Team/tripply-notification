package com.notification.exception;

import com.notification.constant.enums.ErrorConstant;
import com.notification.model.ErrorDetails;
import com.notification.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseModel<String>> handleBadRequestException(BadRequestException ex) {
        log.error("BadRequestException occurred: ", ex);
        ResponseModel<String> errorResponse = new ResponseModel<>();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(List.of(ErrorDetails.builder()
                .errorCode(ErrorConstant.ER003.getErrorCode())
                .errorDesc(ex.getMessage())
                .build()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex) {
        log.error("RecordNotFoundException occurred: ", ex);
        ResponseModel<String> errorResponse = new ResponseModel<>();
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(List.of(ErrorDetails.builder()
                .errorCode(ErrorConstant.ER001.getErrorCode())
                .errorDesc(ex.getMessage())
                .build()));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("Exception occurred: ", ex);
        ResponseModel<String> errorResponse = new ResponseModel<>();
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(List.of(ErrorDetails.builder()
                .errorCode(ErrorConstant.ER003.getErrorCode())
                .errorDesc(ErrorConstant.ER003.getErrorDescription())
                .build()));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
