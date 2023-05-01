package gratis.contoh.api.service;

import gratis.contoh.api.model.request.AuthenticationRequest;
import gratis.contoh.api.model.response.AuthenticationResponse;
import reactor.core.publisher.Mono;

public interface AuthenticationService {
	
	public Mono<AuthenticationResponse> login(Mono<AuthenticationRequest> authenticationRequest);

}
