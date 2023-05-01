package gratis.contoh.api.repository.impl;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import gratis.contoh.api.model.redis.MstLanguageMapping;
import gratis.contoh.api.repository.RedisMstLanguageMappingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RedisMstLanguageMappingRepositoryImpl implements RedisMstLanguageMappingRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisMstLanguageMappingRepositoryImpl.class);
	
	private final ReactiveRedisConnectionFactory factory;
	private final ReactiveRedisOperations<String, MstLanguageMapping> mstLanguageMappingOps;
	
	public RedisMstLanguageMappingRepositoryImpl(
			ReactiveRedisConnectionFactory factory,
			ReactiveRedisOperations<String, MstLanguageMapping> mstLanguageMappingOps) {
		this.mstLanguageMappingOps = mstLanguageMappingOps;
		this.factory = factory;
	}

	@Override
	public Mono<MstLanguageMapping> set(String id, MstLanguageMapping item) {
		return mstLanguageMappingOps.opsForValue().set(id, item)
				.flatMap(res -> mstLanguageMappingOps.opsForValue().get(id));
	}

	@Override
	public Mono<MstLanguageMapping> get(String id) {
		logger.info(id);
		return mstLanguageMappingOps.opsForValue().get(id);
	}

	@Override
	public Flux<MstLanguageMapping> getAll(String pattern) {
		return this.mstLanguageMappingOps
				.keys(pattern + "*")
				.flatMap(key -> get(key));
	}

	@Override
	public Mono<Long> delete(String id) {
		return factory.getReactiveConnection().
				keyCommands().
				del(ByteBuffer.wrap(id.getBytes(Charset.forName("UTF-8"))));
	}

	@Override
	public Flux<Long> deletePattern(String pattern) {
		return this.mstLanguageMappingOps
				.keys(pattern + "*")
				.flatMap(key -> delete(key));
	}
	
}
