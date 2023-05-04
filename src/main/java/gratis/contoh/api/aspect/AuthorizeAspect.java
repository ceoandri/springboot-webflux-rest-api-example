package gratis.contoh.api.aspect;

import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import javax.security.sasl.AuthenticationException;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import gratis.contoh.api.constant.AuthTypes;
import gratis.contoh.api.constant.KeyPattern;
import gratis.contoh.api.model.request.AuthenticationRequest;
import gratis.contoh.api.repository.RedisDefaultRepository;
import gratis.contoh.api.service.AuthenticationService;
import gratis.contoh.api.util.annotation.Authorize;
import gratis.contoh.api.util.jwt.Jwt;
import gratis.contoh.api.util.jwt.JwtDetail;
import gratis.contoh.api.util.web.HttpRequest;
import reactor.core.publisher.Mono;

@Aspect
@Component
public class AuthorizeAspect {
	
	@Value("${app.name}") 
	private String issuer;
	
	@Value("${jtw.secret}") 
	private String secret;
	
	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private RedisDefaultRepository redisDefaultRepository;
	
	@Pointcut("args(request,..)")
	private void serverHttpRequest(ServerHttpRequest request) {}
	
	@Pointcut("@annotation(authorize)")
	private void authorizeData(Authorize authorize) {}

	@Before("authorizeData(authorize) && serverHttpRequest(request)")
	public void before(Authorize authorize, ServerHttpRequest request) 
			throws AccessDeniedException, AuthenticationException, InterruptedException, ExecutionException {
		if (!(request instanceof ServerHttpRequest)) {
			throw new RuntimeException("request should be HttpServletRequesttype");
		}
		
		String headerName = authorize.header();
		String authType = authorize.authType();
		String[] roles = authorize.roles();
		String module = authorize.module();
		String[] accessTypes = authorize.accessTypes();

		String token = HttpRequest.getHeader(request, headerName);
		
		if (token != null) {
			boolean res = this.authorizeToken(authType, token, roles, module, accessTypes).toFuture().get();
			
			if (!res) {
				throw new AccessDeniedException("you don't have permission to access this api");
			}
				
		} else {
			throw new AuthenticationException("please login to access this api");
		}
	}
	
	private Mono<Boolean> authorizeToken(
			String authType, 
			String token, 
			String[] roles, 
			String module, 
			String[] accessTypes) throws AccessDeniedException {
		switch (authType) {
		case AuthTypes.BEARER: {
			if (token.startsWith("Bearer ")) {
				return this.authorizeBearerAuth(token.split(" ")[1], roles, module, accessTypes);				
			} else {
				throw new AccessDeniedException("you don't have permission to access this api");
			}
		}
		case AuthTypes.BASIC: {
			if (token.startsWith("Basic ")) {
				return this.authorizeBasicAuth(token.split(" ")[1], roles, module, accessTypes);				
			} else {
				throw new AccessDeniedException("you don't have permission to access this api");
			}
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + authType);
		}
	}
	
	private Mono<Boolean> authorizeBearerAuth(
			String token, 
			String[] roles, 
			String module, 
			String[] accessTypes) throws AccessDeniedException {
		Jwt jwt = new Jwt(issuer, secret);
		
		JwtDetail detail = jwt.validateAndGetDetail(token);
		if (detail == null) {
			throw new AccessDeniedException("you don't have permission to access this api");
		}
		
		return roleValidation(roles, detail.getRole())
			.zipWith(moduleValidation(module, accessTypes, detail.getRole()))
			.map(tuple -> tuple.getT1() && tuple.getT2());
	}
	
	private Mono<Boolean> authorizeBasicAuth(
			String token,
			String[] roles, 
			String module, 
			String[] accessTypes) throws AccessDeniedException {
		
		byte[] decoded = Base64.getDecoder().decode(token);
		String decodedStr = new String(decoded, StandardCharsets.UTF_8);
		
		String[] data = decodedStr.split(":");
		if (data.length < 2) {
			throw new AccessDeniedException("you don't have permission to access this api");
		}

		return this.authService.login(
				Mono.just(AuthenticationRequest.builder()
						.username(data[0])
						.password(data[1]).build()))
				.flatMap(response -> {
					try {
						return authorizeBearerAuth(response.getAccessToken(), 
								roles, module, accessTypes);
					} catch (AccessDeniedException e) {
						return Mono.just(false);
					}
				});
	}
	
	private Mono<Boolean> roleValidation(String[] roles, String role) 
			throws AccessDeniedException {
		if (roles.length != 0) {
			return isRoleExist(roles, role);
		}
		
		return Mono.just(true);
	}
	
	private Mono<Boolean> isRoleExist(String[] roles, String role) 
			throws AccessDeniedException {
		for(String item : roles) {
			if (item.equals(role)) {
				return Mono.just(true);
			}
		}
		
		throw new AccessDeniedException("you don't have permission to access this api");
	}
	
	private Mono<Boolean> moduleValidation(String module, String[] accessTypes, String role) 
			throws AccessDeniedException {
		if (!module.isBlank() && !module.isEmpty()) {
			return isModuleExist(module, accessTypes, role);
		}
		return Mono.just(true);
	}
	
	private Mono<Boolean> isModuleExist(String module, String[] accessTypes, String role) 
			throws AccessDeniedException {
		String key = KeyPattern.REDIS_ROLE_PERMISSION + role + "__" + module;
		return this.redisDefaultRepository.get(key)
				.zipWhen(permission -> accessTypeValidation(accessTypes, permission))
				.map(tuple -> tuple.getT2());
	}
	
	private Mono<Boolean> accessTypeValidation(String[] accessTypes, String permission) {
		if (accessTypes.length != 0) {
			return isAccessTypeExist(accessTypes, permission);
		}
		
		return Mono.just(true);
	}
	
	private Mono<Boolean> isAccessTypeExist(String[] accessTypes, String permission) {
		for(String accessType : accessTypes) {
			if (permission.contains(accessType)) {
				return Mono.just(true);
			}
		}
		
		return Mono.just(false);
	}
}
