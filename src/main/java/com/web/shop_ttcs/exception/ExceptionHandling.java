package com.web.shop_ttcs.exception;

import com.web.shop_ttcs.exception.ex.ShopNotFoundException;
import com.web.shop_ttcs.exception.ex.UserNotAuthorizedException;
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

    @ExceptionHandler(UserNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDTO userNotAuthorizeException(WebRequest webRequest, UserNotAuthorizedException e) {
        return ExceptionMessageDTO.builder()
                .status(10101L)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ShopNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDTO shopNotFoundException(WebRequest webRequest, ShopNotFoundException e) {
        return ExceptionMessageDTO.builder()
                .status(10100L)
                .message(e.getMessage())
                .build();
    }

}
