package io.chaiyung.imauth.server;

import io.chiayungluk.imauth.common.CanConnectDto;
import io.chiayungluk.imauth.common.CanPublishDto;
import io.chiayungluk.imauth.common.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizeHandler {
    private static final String TOKEN_PREFIX_BEARER = "Bearer ";

    private final String jwtSecret;

    private final BlacklistRepository blacklistRepository;

    public AuthorizeHandler(BlacklistRepository blacklistRepository, AppConfig appConfig) {
        this.jwtSecret = appConfig.getJwtSecret();
        this.blacklistRepository = blacklistRepository;
    }

    public Mono<ServerResponse> canConnect(ServerRequest request) {
        String authHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX_BEARER)) {
                String authToken = authHeader.replace(TOKEN_PREFIX_BEARER, "");
                UserDto userDto = JwtUtil.verifyJWTToken(authToken, jwtSecret);
                return request.bodyToMono(CanConnectDto.class)
                        .flatMap(canConnectDto -> {
                            if (canConnectDto.getUsername().equals(userDto.getUsername())
                                    && canConnectDto.getUserId().equals(userDto.getId())) {
                                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(userDto));
                            } else {
                                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                            }
                        });
            }
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            log.warn("#canConnect exp: ", e);
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public Mono<ServerResponse> canSubscribe(ServerRequest request) {
        // TODO: 2022/12/13  authorize group members
        return ServerResponse.ok().build();
    }

    public Mono<ServerResponse> canPublish(ServerRequest request) {
        return request.bodyToMono(CanPublishDto.class)
                .flatMap(canPublishDto ->
                        Mono.just(canPublishDto.getReceiveUserId().equals(canPublishDto.getSenderUserId()))
                                .or(blacklistRepository.isMember(canPublishDto.getReceiveUserId(),
                                        new UserDto(canPublishDto.getSenderUsername(),
                                                canPublishDto.getSenderUserId())).map(r -> !r)))
                .flatMap(canPublish -> {
                    return canPublish ? ServerResponse.ok().build() :
                            ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                });
    }
}
