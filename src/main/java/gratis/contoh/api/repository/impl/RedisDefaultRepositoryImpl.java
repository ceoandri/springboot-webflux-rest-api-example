package gratis.contoh.api.repository.impl;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import gratis.contoh.api.repository.RedisDefaultRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RedisDefaultRepositoryImpl implements RedisDefaultRepository {
	
	private final ReactiveRedisConnectionFactory factory;
	private final ReactiveRedisOperations<String, String> ops;
	private final Charset charset = Charset.forName("UTF-8");
	
	public RedisDefaultRepositoryImpl(
			ReactiveRedisConnectionFactory factory,
			ReactiveRedisOperations<String, String> ops) {
		this.ops = ops;
		this.factory = factory;
	}
	
	@Override
	public Mono<String> set(String id, String item) {
		return this.ops.opsForValue().getAndSet(id, item);
	}

	@Override
	public Mono<String> get(String id) {
		return this.ops.opsForValue().get(id);
	}

	@Override
	public Flux<String> getAll(String pattern) {
		return this.factory.getReactiveConnection()
				.keyCommands()
				.keys(strToBb("*" + pattern + "*", charset))
				.map(item -> {
					List<String> res = new ArrayList<String>();
					item.forEach(bb -> {
						res.add(bbToStr(bb, charset));
					});
					return res;
				})
				.flatMapMany(keys -> Flux.fromArray(keys.toArray(new String [0])))
				.flatMap(keys -> get(keys));
	}

	@Override
	public Mono<Long> delete(String id) {
		return this.factory.getReactiveConnection()
				.keyCommands()
				.del(strToBb(id, charset));
	}

	@Override
	public Flux<Long> deletePattern(String pattern) {
		return this.ops
				.keys("*" + pattern + "*")
				.flatMap(key -> delete(key));
	}
	
	private ByteBuffer strToBb(String msg, Charset charset){
	    return ByteBuffer.wrap(msg.getBytes(charset));
	}

	private String bbToStr(ByteBuffer buffer, Charset charset){
	    byte[] bytes;
	    if(buffer.hasArray()) {
	        bytes = buffer.array();
	    } else {
	        bytes = new byte[buffer.remaining()];
	        buffer.get(bytes);
	    }
	    return new String(bytes, charset);
	}

}
