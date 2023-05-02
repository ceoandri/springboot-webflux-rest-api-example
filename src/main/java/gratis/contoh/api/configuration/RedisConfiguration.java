package gratis.contoh.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import gratis.contoh.api.model.redis.MstLanguageMapping;

@Configuration
public class RedisConfiguration {
	
	@Bean
	ReactiveRedisOperations<String, MstLanguageMapping> mstLanguageMappingOperations(
			ReactiveRedisConnectionFactory factory) {
		Jackson2JsonRedisSerializer<MstLanguageMapping> serializer = 
				new Jackson2JsonRedisSerializer<>(MstLanguageMapping.class);

	    RedisSerializationContext.RedisSerializationContextBuilder<String, MstLanguageMapping> builder =
	        RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

	    RedisSerializationContext<String, MstLanguageMapping> context = 
	    		builder.value(serializer).build();

	    return new ReactiveRedisTemplate<>(factory, context);
	}

}
