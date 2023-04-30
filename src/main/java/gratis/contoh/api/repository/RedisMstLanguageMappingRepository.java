package gratis.contoh.api.repository;

import gratis.contoh.api.model.redis.MstLanguageMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RedisMstLanguageMappingRepository {
	
	Mono<MstLanguageMapping> set(String id, MstLanguageMapping item);
	Mono<MstLanguageMapping> get(String id);
	Flux<MstLanguageMapping> getAll(String containsId);

}
