package gratis.contoh.api.repository.impl;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import gratis.contoh.api.model.redis.MstLanguageMapping;
import gratis.contoh.api.repository.RedisMstLanguageMappingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RedisMstLanguageMappingRepositoryImpl implements RedisMstLanguageMappingRepository {
	
	private final ReactiveRedisOperations<String, MstLanguageMapping> mstLanguageMappingOps;
	
	public RedisMstLanguageMappingRepositoryImpl(
			ReactiveRedisOperations<String, MstLanguageMapping> mstLanguageMappingOps) {
		this.mstLanguageMappingOps = mstLanguageMappingOps;
	}

	@Override
	public Mono<MstLanguageMapping> set(String id, MstLanguageMapping item) {
		return mstLanguageMappingOps.opsForValue().set(id, item)
				.flatMap(res -> mstLanguageMappingOps.opsForValue().get(id));
	}

	@Override
	public Mono<MstLanguageMapping> get(String id) {
		return mstLanguageMappingOps.opsForValue().get(id);
	}

	@Override
	public Flux<MstLanguageMapping> getAll(String containsId) {
		return this.mstLanguageMappingOps
				.keys("*")
				.filter(item -> item.contains(containsId))
				.flatMap(key -> get(key));
	}
	
}
