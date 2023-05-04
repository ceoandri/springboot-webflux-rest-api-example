package gratis.contoh.api.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gratis.contoh.api.constant.KeyPattern;
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
		
		this.redisDefaultRepository.deletePattern(KeyPattern.REDIS_ROLE_PERMISSION)
		.thenMany(this.reactiveRepository.findAll()
				.flatMap(item -> this.redisDefaultRepository.set(
						KeyPattern.REDIS_ROLE_PERMISSION + item.getRole() + "__" + item.getModule(), 
						item.getPermission())))
		.thenMany(this.redisDefaultRepository.getAll(KeyPattern.REDIS_ROLE_PERMISSION))
		.subscribe(item -> logger.info(item));
	}

}
