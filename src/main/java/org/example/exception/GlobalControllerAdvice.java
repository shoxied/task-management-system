package org.example.exception;

import jakarta.servlet.RequestDispatcher;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.BCException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final ErrorAttributes errorAttributes;

    @ExceptionHandler({TaskNotFoundException.class})
    public ResponseEntity<Object> handleTaskNotFoundException(WebRequest request) {
        return new ResponseEntity<>(getNotFoundStringObjectMap(request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(WebRequest request) {
        return new ResponseEntity<>(getNotFoundStringObjectMap(request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(WebRequest request) {
        return new ResponseEntity<>(getBadStringObjectMap(request), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getNotFoundStringObjectMap(WebRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value(), RequestAttributes.SCOPE_REQUEST);
        String uriTemplateVariablesAttribute = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (uriTemplateVariablesAttribute != null) {
            request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, uriTemplateVariablesAttribute, RequestAttributes.SCOPE_REQUEST);
        }
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults()
                .including(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS);
        return errorAttributes.getErrorAttributes(request, options);
    }

    private Map<String, Object> getBadStringObjectMap(WebRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.BAD_REQUEST.value(), RequestAttributes.SCOPE_REQUEST);
        String uriTemplateVariablesAttribute = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (uriTemplateVariablesAttribute != null) {
            request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, uriTemplateVariablesAttribute, RequestAttributes.SCOPE_REQUEST);
        }
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults()
                .including(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS);
        return errorAttributes.getErrorAttributes(request, options);
    }
}
