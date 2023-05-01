package gratis.contoh.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;

import gratis.contoh.api.model.JwtDetail;
import gratis.contoh.api.model.MstUser;
import gratis.contoh.api.model.request.AuthenticationRequest;
import gratis.contoh.api.model.response.AuthenticationResponse;
import gratis.contoh.api.repository.MstUserRepository;
import gratis.contoh.api.service.AuthenticationService;
import gratis.contoh.api.util.jwt.Jwt;
import gratis.contoh.api.util.mapper.ObjectMapper;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Value("${app.name}") 
	private String issuer;
	
	@Value("${jtw.secret}") 
	private String secret;
	
	@Autowired
	private MstUserRepository mstUserRepo;

	@Override
	public Mono<AuthenticationResponse> login(Mono<AuthenticationRequest> authenticationRequest) {
		ObjectMapper<JwtDetail, MstUser> mapper = 
				new ObjectMapper<JwtDetail, MstUser>();
		
		return authenticationRequest
				.zipWhen(request -> this.mstUserRepo.findById(request.getUsername()))
				.flatMap(tuple -> {
					AuthenticationRequest req = tuple.getT1();
					Boolean isRememberMe = req.getRememberMe();
					MstUser user = tuple.getT2();
					
					if (!encoder.matches(req.getPassword(), user.getPassword())) {
						return Mono.justOrEmpty(null);
					}
					
					Jwt jwtToken = new Jwt(issuer, Algorithm.HMAC512(secret));
					
					String token = jwtToken.generateToken(
							mapper.convert(JwtDetail.class, user), isRememberMe);
					
					return Mono.just(new AuthenticationResponse(token));
				});
	}

}
