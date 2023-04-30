package gratis.contoh.api.aspect;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import gratis.contoh.api.util.annotation.Authorize;

@Aspect
@Component
public class AuthorizeAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorizeAspect.class);
	
	@Pointcut("@annotation(gratis.contoh.api.util.annotation.Authorize) && args(request,..)")
	private void serverHttpRequest(ServerHttpRequest request) {}
	
	@Pointcut("@annotation(authorize)")
	private void authorizeData(Authorize authorize) {}

	@Before("authorizeData(authorize) && serverHttpRequest(request)")
	public void before(Authorize authorize, ServerHttpRequest request) throws AccessDeniedException{
		if (!(request instanceof ServerHttpRequest)) {
			throw new RuntimeException("request should be HttpServletRequesttype");
		}
		
		String headerName = authorize.header();
		String[] roles = authorize.roles();
		String[] modules = authorize.modules();
		String[] accessTypes = authorize.accessTypes();
		
		logger.info("header name " + headerName);
		
		for (int i = 0 ; i < roles.length; i++) {
			logger.info("roles name " + roles[i]);
		}
		
		for (int i = 0 ; i < modules.length; i++) {
			logger.info("modules name " + modules[i]);
		}
		
		for (int i = 0 ; i < accessTypes.length; i++) {
			logger.info("accessTypes name " + accessTypes[i]);
		}

		HttpHeaders headers = request.getHeaders();
		List<String> token = headers.get(headerName);
		
		if (token != null) {
			for (int i = 0 ; i < token.size(); i++) {
				logger.info("token " + token.get(i));
			}
			
			// TODO add implementation for check token
		} else {
			throw new AccessDeniedException("you don't have permission to access this api");
		}
	} 
}
