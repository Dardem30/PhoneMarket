package com.itacwt.phonemarket.controller.util;

import com.itacwt.phonemarket.beans.exceptions.ResponseValidationException;
import com.itacwt.phonemarket.controller.response_forms.ResponseForm;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Log log = LogFactory.getLog(getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ResponseForm exception(HttpServletRequest req, Exception ex) {
        ResponseForm responseForm = null;
        try {
            Throwable rootCause = ex.getCause();
            if (rootCause != null) {
                while (rootCause.getCause() != null && rootCause.getCause() != rootCause)
                    rootCause = rootCause.getCause();
                log.error("The request " + req.getRequestURL() + " produced the Exception in Class: " + rootCause.getStackTrace()[0].getClassName() + ", Method: " + rootCause.getStackTrace()[0].getMethodName() + "", ex);
            } else {
                log.error("The request " + req.getRequestURL() + " produced the Exception", ex);
            }

            responseForm = ResponseForm.failure(ex.getMessage());
        } catch (Exception e) {
            log.error("Error during trying to handle exception of the request:" + req.getRequestURL(), e);
        }
        return responseForm;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResponseValidationException.class)
    public @ResponseBody
    ResponseForm handleResponseValidationException(ResponseValidationException ex) {
        return ResponseForm.failure(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    ResponseForm handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        final List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        final Map<String, String> errors = new HashMap<>(allErrors.size());
        allErrors.forEach((error) -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseForm.failureWithResult(errors);
    }
}
