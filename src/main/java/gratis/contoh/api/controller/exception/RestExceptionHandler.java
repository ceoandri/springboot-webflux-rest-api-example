package gratis.contoh.api.controller.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import gratis.contoh.api.model.response.BaseResponse;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ResponseEntity<Object>> handleMissingRequestValueException(WebExchangeBindException ex) {
		List<String> errors = new ArrayList<String>();
		
		ex.getFieldErrors().forEach(item -> errors.add(item.getField() + " " + item.getDefaultMessage()));
	    
		BaseResponse<String> errorResponse = BaseResponse.<String>builder()
				.errors(errors)
				.message(HttpStatus.BAD_REQUEST.name())
				.status(HttpStatus.BAD_REQUEST.value())
				.build();
	    return Mono.just(ResponseEntity
	    		.status(HttpStatus.BAD_REQUEST)
	    		.body(errorResponse));
	}
}
