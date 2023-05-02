package gratis.contoh.api.service;

import gratis.contoh.api.model.response.PermissionResponse;
import reactor.core.publisher.Flux;

public interface AccountService {
	
	public Flux<PermissionResponse> currentPermission(String authToken);

}
