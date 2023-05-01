package gratis.contoh.api.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RedisDefaultRepository {

	Mono<String> set(String id, String item);
	Mono<String> get(String id);
	Flux<String> getAll(String pattern);
	Mono<Long> delete(String id);
	Flux<Long> deletePattern(String pattern);
	
}
