package io.chaiyung.imauth.server;

import io.chiayungluk.imauth.common.UserDto;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BlacklistRepository {
    private final ReactiveRedisOperations<String, UserDto> blacklistOps;

    public BlacklistRepository(ReactiveRedisOperations<String, UserDto> blacklistOps) {
        this.blacklistOps = blacklistOps;
    }

    public Flux<UserDto> get(String userId) {
        return blacklistOps.opsForSet().members(userId);
    }

    public Mono<Boolean> isMember(String userId, UserDto userDto) {
        return blacklistOps.opsForSet().isMember(userId, userDto);
    }

    public Mono<Boolean> add(String userId, UserDto userDto) {
        return blacklistOps.opsForSet().add(userId, userDto).map(l -> true);
    }

    public Mono<Boolean> remove(String userId, UserDto userDto) {
        return blacklistOps.opsForSet().remove(userId, userDto).map(l -> true);
    }
}
