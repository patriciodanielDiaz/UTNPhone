package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.dto.ErrorDto;
import com.utn.UTN.Phone.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@RestControllerAdvice
 public class ControllerAdvice extends ResponseEntityExceptionHandler {

        @ResponseStatus(HttpStatus.FORBIDDEN)
        @ExceptionHandler(PermissionDeniedException.class)
        public ErrorDto handlePermissionDeniedException(PermissionDeniedException exc) { return new ErrorDto(0, "Permission Denied"); }

        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        @ExceptionHandler(InvalidLoginException.class)
        public ErrorDto handleLoginException(InvalidLoginException exc) {
            return new ErrorDto(1, "Invalid login");
        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(ValidationException.class)
        public ErrorDto handleValidationException(ValidationException exc) {
            return new ErrorDto(2,"incomplete data");
        }

        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        @ExceptionHandler(UserNotExistException.class)
        public ErrorDto handleUserNotExists() { return new ErrorDto(3, "User not exists"); }

        @ResponseStatus(HttpStatus.NO_CONTENT)
        @ExceptionHandler(RecordNotExistsException.class)
        public ErrorDto handleRecordNotExistsException() { return new ErrorDto(4, "No Content"); }

        @ResponseStatus(HttpStatus.CONFLICT)
        @ExceptionHandler(DuplicateDNI.class)
        public ErrorDto handleDuplicateDNI() { return new ErrorDto(5, "Failing validation :duplicate DNI "); }

        @ResponseStatus(HttpStatus.CONFLICT)
        @ExceptionHandler(DuplicateUserName.class)
        public ErrorDto handleDuplicateUserName() { return new ErrorDto(6, "Failing validation :duplicate UserName "); }

       @ResponseStatus(HttpStatus.CONFLICT)
       @ExceptionHandler(SQLException.class)
       public ErrorDto handleSQLexception() { return new ErrorDto(7, "incomplete data"); }

       @ResponseStatus(HttpStatus.BAD_REQUEST)
       @ExceptionHandler(LineNotExistsException.class)
       public ErrorDto handleLineNotExistsException(LineNotExistsException e) {return new ErrorDto(8,"line not exists"); }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(LineTypeNotExistsException.class)
        public ErrorDto handleLineTypeNotExistsException(LineTypeNotExistsException e) {return new ErrorDto(9,"line Type not exists"); }


        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(CityNotExistsException.class)
        public ErrorDto handleCityNotExistsException(CityNotExistsException e) {return new ErrorDto(10,"city not exists"); }

        @ResponseStatus(HttpStatus.CONFLICT)
        @ExceptionHandler(IncorrectDateException.class)
        public ErrorDto handleIncorrectDateException() { return new ErrorDto(11, "date out of rank"); }

}
