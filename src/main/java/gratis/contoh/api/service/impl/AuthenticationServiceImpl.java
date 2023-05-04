package gratis.contoh.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gratis.contoh.api.constant.Roles;
import gratis.contoh.api.controller.exception.BadRequestException;
import gratis.contoh.api.model.MstUser;
import gratis.contoh.api.model.dto.MstUserDto;
import gratis.contoh.api.model.request.AuthenticationRequest;
import gratis.contoh.api.model.response.AuthenticationResponse;
import gratis.contoh.api.model.response.MstUserResponse;
import gratis.contoh.api.repository.MstUserRepository;
import gratis.contoh.api.service.AuthenticationService;
import gratis.contoh.api.util.jwt.Jwt;
import gratis.contoh.api.util.jwt.JwtDetail;
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
					Boolean isRememberMe = req.getRememberMe() == null ? false : req.getRememberMe();
					MstUser user = tuple.getT2();
					
					if (!encoder.matches(req.getPassword(), user.getPassword())) {
						return Mono.justOrEmpty(null);
					}
					
					Jwt jwtToken = new Jwt(issuer, secret);
					
					String token = jwtToken.generateToken(
							mapper.convert(JwtDetail.class, user), isRememberMe);
					
					return Mono.just(new AuthenticationResponse(token));
				});
	}

	@Override
	public Mono<MstUserResponse> init(Mono<MstUserDto> userDto) {
		ObjectMapper<MstUser, MstUserDto> mapper = 
				new ObjectMapper<MstUser, MstUserDto>();
		
		ObjectMapper<MstUserResponse, MstUser> responseMapper = 
				new ObjectMapper<MstUserResponse, MstUser>();
		
		return userDto
				.zipWhen(dto -> {
					dto.setPassword(encoder.encode(dto.getPassword()));
					if (!checkRole(dto.getRole())) {
						return Mono.error(
								new BadRequestException(
										"Role must be one of this item: " + roleList()));
					}
					
					return this.mstUserRepo.findById(dto.getUsername());
				})
				.flatMap(tuple -> {
					if (tuple.getT2() != null) {
						return Mono.error(
								new BadRequestException(
										"Username " + tuple.getT2().getUsername() + " already exist"));
					}
						
					return this.mstUserRepo.save(mapper.convert(MstUser.class, tuple.getT1()));
				})
				.map(user -> responseMapper.convert(MstUserResponse.class, user));
	}
	
	private boolean checkRole(String role) {
		switch (role) {
		case Roles.GUEST: {
			return true;
		}
		case Roles.ADMIN: {
			return true;
		}
		case Roles.SUPER_ADMIN: {
			return true;
		}
		default:
			return false;
		}
	}
	
	private String roleList() {
		return Roles.GUEST + " | " + Roles.ADMIN + " | " + Roles.SUPER_ADMIN;
	}

}
