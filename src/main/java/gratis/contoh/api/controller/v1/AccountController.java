package gratis.contoh.api.controller.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gratis.contoh.api.model.response.BaseResponse;
import gratis.contoh.api.model.response.PermissionResponse;
import gratis.contoh.api.model.response.ProfileResponse;
import gratis.contoh.api.service.AccountService;
import gratis.contoh.api.util.annotation.Authorize;
import gratis.contoh.api.util.web.HttpRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
	
	@Autowired
	private AccountService accService;
	
	@GetMapping("/permission")
	@Tag(name = "Get Current Permission", description = "Retrive all data permission of the user")
	@Authorize
	public Mono<ResponseEntity<BaseResponse<List<PermissionResponse>>>> getCurrentPermission(
    		ServerHttpRequest serverHttpRequest) {
		List<String> error = new ArrayList<String>();
		error.add("you don't have permission to access this api");
		
		String token = HttpRequest.getHeader(serverHttpRequest, HttpHeaders.AUTHORIZATION);

        return accService.currentPermission(token)
        		.collectList()
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<List<PermissionResponse>>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.FORBIDDEN)
        				.body(BaseResponse.<List<PermissionResponse>>builder()
        						.status(HttpStatus.FORBIDDEN.value())
        						.message(HttpStatus.FORBIDDEN.name())
        						.errors(error)
        						.build()));
	}
	
	@GetMapping("/profile")
	@Tag(name = "Get Current Permission", description = "Retrive all data permission of the user")
	@Authorize
	public Mono<ResponseEntity<BaseResponse<ProfileResponse>>> getProfile(
    		ServerHttpRequest serverHttpRequest) {
		List<String> error = new ArrayList<String>();
		error.add("you don't have permission to access this api");
		
		String token = HttpRequest.getHeader(serverHttpRequest, HttpHeaders.AUTHORIZATION);
		
        return accService.profile(token)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<ProfileResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.FORBIDDEN)
        				.body(BaseResponse.<ProfileResponse>builder()
        						.status(HttpStatus.FORBIDDEN.value())
        						.message(HttpStatus.FORBIDDEN.name())
        						.errors(error)
        						.build()));
	}
	
}
