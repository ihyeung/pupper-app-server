//package com.utahmsd.pupper.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.server.MethodNotAllowedException;
//
//import javax.ws.rs.BadRequestException;
//import javax.ws.rs.ForbiddenException;
//import javax.xml.bind.ValidationException;
//
//public class ExceptionMapper {
//    public static RuntimeException mapException(final Throwable throwable){
////        ForbiddenException, ValidationException, MethodNotAllowedException,
//        if (throwable instanceof BadRequestException){
//            return new RuntimeException(throwable.getMessage(), HttpStatus.BAD_REQUEST.value(), "400", "", "");
//        } else if (throwable instanceof ValidationException || throwable instanceof PaymentInstrumentNotFoundException){
//            return new RestException(throwable.getMessage(), HttpStatus.NOT_FOUND.value(), "404", "PaymentInstrument not found", "");
//        }
//        return new RestException(throwable.getMessage(), HttpStatus.BAD_REQUEST.value(), "400", "General Error", "");
//    }
//}