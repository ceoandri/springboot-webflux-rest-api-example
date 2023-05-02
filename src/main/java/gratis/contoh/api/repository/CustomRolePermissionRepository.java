package gratis.contoh.api.repository;

import java.util.ArrayList;

import gratis.contoh.api.model.RolePermission;
import gratis.contoh.api.util.querybuilder.Criteria;
import reactor.core.publisher.Flux;

public interface CustomRolePermissionRepository {
	
	Flux<RolePermission> findAll();
	
	Flux<RolePermission> findAll(ArrayList<Criteria> criterias);

}
