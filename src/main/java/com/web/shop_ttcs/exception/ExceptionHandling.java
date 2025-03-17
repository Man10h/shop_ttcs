package com.web.shop_ttcs.exception;

import com.web.shop_ttcs.exception.ex.UserNotFoundException;
import com.web.shop_ttcs.model.dto.ExceptionMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDTO userNotFoundException(WebRequest webRequest, UserNotFoundException e) {
        return ExceptionMessageDTO.builder()
                .status(10100L)
                .message(e.getMessage())
                .build();
    }

}
