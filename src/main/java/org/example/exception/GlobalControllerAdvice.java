package org.example.exception;

import jakarta.servlet.RequestDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity<Object> handleConstraintViolationException(WebRequest request) {
        return new ResponseEntity<>(getStringObjectMap(request), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getStringObjectMap(WebRequest request) {
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
