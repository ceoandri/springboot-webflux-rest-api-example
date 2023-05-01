package gratis.contoh.api.repository;

import gratis.contoh.api.model.RolePermission;
import reactor.core.publisher.Flux;

public interface CustomRolePermissionRepository {
	
	Flux<RolePermission> findAll();

}
