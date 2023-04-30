package gratis.contoh.api.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gratis.contoh.api.model.redis.MstLanguageMapping;
import gratis.contoh.api.repository.MstLanguageMappingRepository;
import gratis.contoh.api.repository.RedisMstLanguageMappingRepository;
import gratis.contoh.api.util.mapper.ObjectMapperUtil;
import jakarta.annotation.PostConstruct;

@Component
public class MstLanguageMappingLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(MstLanguageMappingLoader.class);

	@Autowired
	private RedisMstLanguageMappingRepository redisRepository;
	
	@Autowired
	private MstLanguageMappingRepository reactiveRepository;
	
	@PostConstruct
	public void loadData() {
		ObjectMapperUtil<MstLanguageMapping, gratis.contoh.api.model.MstLanguageMapping> mapper = 
				new ObjectMapperUtil<MstLanguageMapping, gratis.contoh.api.model.MstLanguageMapping>();
		
		this.reactiveRepository.findAllByDeletedAtIsNull()
				.map(item -> mapper.convert(MstLanguageMapping.class, item))
				.flatMap(item -> this.redisRepository.set("mst_language_mapping__" + item.getMapping(), item))
				.thenMany(this.redisRepository.getAll("mst_language_mapping__"))
				.subscribe(item -> logger.info(
						"Key : " + item.getMapping() + ", Id: " + item.getId() + ", En: " + item.getEn()));
		
	}

}
