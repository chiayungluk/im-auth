package io.chaiyung.imauth.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class BlacklistRouter {
    @Bean
    public RouterFunction<ServerResponse> blacklistRoute(BlacklistHandler blacklistHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/blacklist/{userId}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        blacklistHandler::getBlack)
                .andRoute(RequestPredicates.POST("/blacklist/{userId}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        blacklistHandler::addBlack)
                .andRoute(RequestPredicates.DELETE("/blacklist/{userId}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        blacklistHandler::removeBlack);
    }

}
