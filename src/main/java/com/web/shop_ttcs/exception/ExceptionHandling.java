package com.web.shop_ttcs.exception;

import com.web.shop_ttcs.exception.ex.*;
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

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDTO productNotFoundException(WebRequest webRequest, ProductNotFoundException e) {
        return ExceptionMessageDTO.builder()
                .status(10100L)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDTO refreshTokenNotFoundException(WebRequest webRequest, RefreshTokenNotFoundException e) {
        return ExceptionMessageDTO.builder()
                .status(10100L)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDTO cartItemNotFoundException(WebRequest webRequest, CartItemNotFoundException e) {
        return ExceptionMessageDTO.builder()
                .status(10100L)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(RatingNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ExceptionMessageDTO ratingNotFoundException(WebRequest webRequest, RatingNotFoundException e) {
        return ExceptionMessageDTO.builder()
                .status(10100L)
                .message(e.getMessage())
                .build();
    }

}
