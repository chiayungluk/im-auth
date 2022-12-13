package io.chaiyung.imauth.server;

import io.chiayungluk.imauth.common.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BlacklistHandler {
    private final BlacklistRepository blacklistRepository;

    public BlacklistHandler(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    public Mono<ServerResponse> getBlack(ServerRequest request) {
        String userId = request.pathVariable("userId");
        return blacklistRepository.get(userId)
                .collectList()
                .flatMap(userDtos -> ServerResponse.ok().bodyValue(userDtos));
    }


    public Mono<ServerResponse> addBlack(ServerRequest request) {
        String userId = request.pathVariable("userId");
        return request.bodyToMono(UserDto.class)
                .flatMap(userDto -> blacklistRepository.add(userId, userDto))
                .flatMap(r -> r ? ServerResponse.ok().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public Mono<ServerResponse> removeBlack(ServerRequest request) {
        String userId = request.pathVariable("userId");
        return request.bodyToMono(UserDto.class)
                .flatMap(userDto -> blacklistRepository.remove(userId, userDto))
                .flatMap(r -> r ? ServerResponse.ok().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
