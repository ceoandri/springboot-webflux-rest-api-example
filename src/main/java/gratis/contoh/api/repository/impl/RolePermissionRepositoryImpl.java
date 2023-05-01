package gratis.contoh.api.repository.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import gratis.contoh.api.model.RolePermission;
import gratis.contoh.api.repository.CustomRolePermissionRepository;
import gratis.contoh.api.repository.query.RolePermissionQuery;
import gratis.contoh.api.util.mapper.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RolePermissionRepositoryImpl implements CustomRolePermissionRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(RolePermissionRepositoryImpl.class);
	
	private DatabaseClient databaseClient;

    public RolePermissionRepositoryImpl(DatabaseClient databaseClient) {
    	this.databaseClient = databaseClient;
	}

	@Override
	public Flux<RolePermission> findAll() {
		String query = RolePermissionQuery.getRolePermission;
		
		logger.info("preparing for execute statement " + query);

		ObjectMapper<RolePermission, Map<String, Object>> mapper = 
				new ObjectMapper<RolePermission, Map<String, Object>>();
		
		return databaseClient.sql(query)
                .fetch()
                .all()
                .flatMap(item -> Mono.just(mapper.convert(RolePermission.class, item)));
	}

}
