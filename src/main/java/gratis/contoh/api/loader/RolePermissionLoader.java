package gratis.contoh.api.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gratis.contoh.api.repository.CustomRolePermissionRepository;
import gratis.contoh.api.repository.RedisDefaultRepository;
import jakarta.annotation.PostConstruct;

@Component
public class RolePermissionLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(RolePermissionLoader.class);

	@Autowired
	private RedisDefaultRepository redisDefaultRepository;

	@Autowired
	private CustomRolePermissionRepository reactiveRepository;
	
	@PostConstruct
	public void loadData() {
		logger.info("Preparing for fetching role permission data");
		
		this.redisDefaultRepository.deletePattern("role_permission__")
		.thenMany(this.reactiveRepository.findAll()
				.flatMap(item -> this.redisDefaultRepository.set(
						"role_permission__" + item.getRole() + "__" + item.getModule(), 
						item.getPermission())))
		.thenMany(this.redisDefaultRepository.getAll("role_permission__"))
		.subscribe(item -> logger.info(item));
	}

}
