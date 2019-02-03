package com.hackorama.flags.server.spring;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hackorama.flags.common.HttpMethod;

@Configuration
public class Router {

    private RouterFunction<ServerResponse> routerFunction;

    private static Logger logger = LoggerFactory.getLogger(Router.class);

    @Bean
    public RouterFunction<ServerResponse> route(Handler handler) {
        logger.info("Routing {}", handler.toString());
        routerFunction = RouterFunctions
                .route(RequestPredicates.GET("/").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), request -> {
                    try {
                        return handler.router(request);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                            | InterruptedException | ExecutionException e) {
                        e.printStackTrace(); //TODO Log and report
                    }
                    return null; //TODO Return error message
                });
        Handler.getHandlerMap().get(HttpMethod.GET).forEach((k, v) -> {
            routerFunction = routerFunction
                    .andRoute(RequestPredicates.GET(k).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), request -> {
                        try {
                            return handler.router(request);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                                | InterruptedException | ExecutionException e) {
                            e.printStackTrace(); //TODO Log and report
                        }
                    return null; //TODO Return error message
                    });
        });
        Handler.getHandlerMap().get(HttpMethod.POST).forEach((k, v) -> {
            routerFunction = routerFunction.andRoute(
                    RequestPredicates.POST(k).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), request -> {
                        try {
                            return handler.router(request);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                                | InterruptedException | ExecutionException e) {
                              e.printStackTrace(); //TODO Log and report
                        }
                    return null; //TODO Return error message
                    });
        });
        Handler.getHandlerMap().get(HttpMethod.PUT).forEach((k, v) -> {
            routerFunction = routerFunction
                    .andRoute(RequestPredicates.PUT(k).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), request -> {
                        try {
                            return handler.router(request);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                                | InterruptedException | ExecutionException e) {
                              e.printStackTrace(); //TODO Log and report
                        }
                    return null; //TODO Return error message
                    });
        });
        Handler.getHandlerMap().get(HttpMethod.DELETE).forEach((k, v) -> {
            routerFunction = routerFunction.andRoute(
                    RequestPredicates.DELETE(k).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), request -> {
                        try {
                            return handler.router(request);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                                | InterruptedException | ExecutionException e) {
                              e.printStackTrace(); //TODO Log and report
                        }
                    return null; //TODO Return error message
                    });
        });
        return routerFunction;
    }

}
