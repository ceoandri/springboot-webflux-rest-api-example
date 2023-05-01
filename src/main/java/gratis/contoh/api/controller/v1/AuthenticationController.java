package gratis.contoh.api.controller.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gratis.contoh.api.model.request.AuthenticationRequest;
import gratis.contoh.api.model.response.AuthenticationResponse;
import gratis.contoh.api.model.response.BaseResponse;
import gratis.contoh.api.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authService;
	
	@PostMapping("/login")
	@Tag(name = "Login", description = "login user")
	public Mono<ResponseEntity<BaseResponse<AuthenticationResponse>>> post(
    		ServerHttpRequest serverHttpRequest,
    		@RequestBody @Valid Mono<AuthenticationRequest> request) {
		List<String> error = new ArrayList<String>();
		error.add("username or password incorrect");
		
        return authService.login(request)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<AuthenticationResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.UNAUTHORIZED.value())
        				.body(BaseResponse.<AuthenticationResponse>builder()
        						.status(HttpStatus.UNAUTHORIZED.value())
        						.message(HttpStatus.UNAUTHORIZED.name())
        						.errors(error)
        						.build()));
	}

}
