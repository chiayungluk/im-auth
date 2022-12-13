package io.chaiyung.imauth.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class AuthorizeRouter {
    @Bean
    public RouterFunction<ServerResponse> authorizeRoute(AuthorizeHandler authorizeHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/canConnect")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        authorizeHandler::canConnect)
                .andRoute(RequestPredicates.POST("/canSubscribe")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        authorizeHandler::canSubscribe)
                .andRoute(RequestPredicates.POST("/canPublish")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        authorizeHandler::canPublish);
    }
}
