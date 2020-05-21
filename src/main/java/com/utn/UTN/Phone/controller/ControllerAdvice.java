package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.dto.ErrorDto;
import com.utn.UTN.Phone.exceptions.InvalidLoginException;
import com.utn.UTN.Phone.exceptions.NoDataFound;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
 public class ControllerAdvice extends ResponseEntityExceptionHandler {

        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        @ExceptionHandler(InvalidLoginException.class)
        public ErrorDto handleLoginException(InvalidLoginException exc) {
            return new ErrorDto(1, "Invalid login");
        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(ValidationException.class)
        public ErrorDto handleValidationException(ValidationException exc) {
            return new ErrorDto(2,"username and password must have a value");
        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(UserNotExistException.class)
        public ErrorDto handleUserNotExists() {
            return new ErrorDto(3, "User not exists");
        }

        @ResponseStatus(HttpStatus.NO_CONTENT)
        @ExceptionHandler(NoDataFound.class)
        public ErrorDto handleNoContent() {

            return new ErrorDto(4, "No Content");
        }

    }
