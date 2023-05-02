package gratis.contoh.api.service;

import gratis.contoh.api.model.response.PermissionResponse;
import gratis.contoh.api.model.response.ProfileResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
	
	public Flux<PermissionResponse> currentPermission(String token);
	public Mono<ProfileResponse> profile(String token);

}
