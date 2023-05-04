package gratis.contoh.api.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import gratis.contoh.api.model.response.PermissionResponse;
import gratis.contoh.api.model.response.ProfileResponse;
import gratis.contoh.api.repository.CustomRolePermissionRepository;
import gratis.contoh.api.service.AccountService;
import gratis.contoh.api.util.jwt.Jwt;
import gratis.contoh.api.util.jwt.JwtDetail;
import gratis.contoh.api.util.mapper.ObjectMapper;
import gratis.contoh.api.util.querybuilder.Condition;
import gratis.contoh.api.util.querybuilder.Criteria;
import gratis.contoh.api.util.querybuilder.Operator;
import gratis.contoh.api.util.querybuilder.ValueType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Value("${app.name}") 
	private String issuer;
	
	@Value("${jtw.secret}") 
	private String secret;
	
	@Autowired
	private CustomRolePermissionRepository permissionRepository;

	@Override
	public Flux<PermissionResponse> currentPermission(String token) {
		return validateToken(token)
				.flux()
				.map(item -> {
					ArrayList<Criteria> criterias = new ArrayList<Criteria>();
					criterias.add(new Criteria(
							Operator.AND, 
							Condition.EQUAL, 
							"role", 
							new String[]{item.getRole()}, 
							ValueType.TEXT));
					
					return criterias;
				})
				.flatMap(criterias -> this.permissionRepository.findAll(criterias)
						.map(item -> {
							return PermissionResponse.builder()
									.module(item.getModule())
									.permission(item.getPermission().split(","))
									.build();
						}));
	}

	@Override
	public Mono<ProfileResponse> profile(String token) {
		ObjectMapper<ProfileResponse, JwtDetail> mapper = 
				new ObjectMapper<ProfileResponse, JwtDetail>();

		return validateToken(token)
				.map(item -> mapper.convert(ProfileResponse.class, item));
	}
	
	private Mono<JwtDetail> validateToken(String token) {
		Jwt jwt = new Jwt(issuer, secret);
		
		if (!token.startsWith("Bearer ")) {	
			return Mono.just(new JwtDetail()).filter(item -> item.getUsername() != null);
		}
		
		JwtDetail detail = jwt.validateAndGetDetail(token.split(" ")[1]);
		
		if (detail == null) {	
			return Mono.just(new JwtDetail()).filter(item -> item.getUsername() != null);
		}
		
		return Mono.just(detail);
	}

}
