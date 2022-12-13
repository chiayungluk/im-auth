package io.chaiyung.imauth.server;

import io.chiayungluk.imauth.common.UserDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class UsetDtoConfiguration {

    @Bean
    ReactiveRedisOperations<String, UserDto> redisOperations(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext.RedisSerializationContextBuilder<String, UserDto> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, UserDto> context =
                builder.value(new Jackson2JsonRedisSerializer<>(UserDto.class)).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}