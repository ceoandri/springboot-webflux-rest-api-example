package gratis.contoh.api.service;

import gratis.contoh.api.model.dto.MstUserDto;
import gratis.contoh.api.model.request.AuthenticationRequest;
import gratis.contoh.api.model.response.AuthenticationResponse;
import gratis.contoh.api.model.response.MstUserResponse;
import reactor.core.publisher.Mono;

public interface AuthenticationService {
	
	public Mono<AuthenticationResponse> login(Mono<AuthenticationRequest> authenticationRequest);
	public Mono<MstUserResponse> init(Mono<MstUserDto> userDto);

}
