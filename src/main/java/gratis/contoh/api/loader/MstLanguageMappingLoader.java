package gratis.contoh.api.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gratis.contoh.api.constant.KeyPattern;
import gratis.contoh.api.model.redis.MstLanguageMapping;
import gratis.contoh.api.repository.MstLanguageMappingRepository;
import gratis.contoh.api.repository.RedisDefaultRepository;
import gratis.contoh.api.repository.RedisMstLanguageMappingRepository;
import gratis.contoh.api.util.mapper.ObjectMapper;
import jakarta.annotation.PostConstruct;

@Component
public class MstLanguageMappingLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(MstLanguageMappingLoader.class);

	@Autowired
	private RedisDefaultRepository redisDefaultRepository;

	@Autowired
	private RedisMstLanguageMappingRepository redisRepository;
	
	@Autowired
	private MstLanguageMappingRepository reactiveRepository;
	
	@PostConstruct
	public void loadData() {
		ObjectMapper<MstLanguageMapping, gratis.contoh.api.model.MstLanguageMapping> mapper = 
				new ObjectMapper<MstLanguageMapping, gratis.contoh.api.model.MstLanguageMapping>();
		
		logger.info("Preparing for fetching mst_language_mapping data");
		
		this.redisDefaultRepository.deletePattern(KeyPattern.REDIS_MST_LANGUAGE_MAPPING)
		.thenMany(this.reactiveRepository.findAllByDeletedAtIsNull()
				.map(item -> mapper.convert(MstLanguageMapping.class, item))
				.flatMap(item -> this.redisRepository.set(
						KeyPattern.REDIS_MST_LANGUAGE_MAPPING + item.getMapping(), item))
		)
		.thenMany(this.redisRepository.getAll(KeyPattern.REDIS_MST_LANGUAGE_MAPPING))
		.subscribe(item -> logger.info(
				"Key : " + item.getMapping() + ", Id: " + item.getId() + ", En: " + item.getEn()));
		
	}

}
