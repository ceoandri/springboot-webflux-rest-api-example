package gratis.contoh.api.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;

import gratis.contoh.api.model.response.PermissionResponse;
import gratis.contoh.api.repository.CustomRolePermissionRepository;
import gratis.contoh.api.service.AccountService;
import gratis.contoh.api.util.jwt.Jwt;
import gratis.contoh.api.util.jwt.JwtDetail;
import gratis.contoh.api.util.querybuilder.Condition;
import gratis.contoh.api.util.querybuilder.Criteria;
import gratis.contoh.api.util.querybuilder.Operator;
import gratis.contoh.api.util.querybuilder.ValueType;
import reactor.core.publisher.Flux;

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
		Jwt jwt = new Jwt(issuer, Algorithm.HMAC512(secret));
		
		if (!token.startsWith("Bearer ")) {	
			return Flux.just(new PermissionResponse());
		}
		
		JwtDetail detail = jwt.validateAndGetDetail(token.split(" ")[1]);
		
		ArrayList<Criteria> criterias = new ArrayList<Criteria>();
		criterias.add(new Criteria(
				Operator.AND, 
				Condition.EQUAL, 
				"role", 
				new String[]{detail.getRole()}, 
				ValueType.TEXT));
		
		return this.permissionRepository.findAll(criterias)
				.map(item -> {
					return PermissionResponse.builder()
							.module(item.getModule())
							.permission(item.getPermission().split(","))
							.build();
				});
	}

}
