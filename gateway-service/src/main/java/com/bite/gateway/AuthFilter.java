package com.bite.gateway;

import com.bite.common.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import com.bite.common.pojo.Result;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;

import java.util.List;
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {


    //
    // ObjectMapper 是 Jackson 提供的 JSON 转换工具。
    //这里主要用于将 Java 对象转换成 JSON 格式
    @Autowired
    private ObjectMapper objectMapper;

    private List<String> whiteNames = List.of("/user/login", "/user/register");

    //exchange	当前请求和响应的上下文，可以获取请求信息、响应信息,chain	过滤器链，负责继续执行后续过滤器和请求转发
    //Mono<Void> 是 Spring WebFlux 使用的响应式返回类型，表示异步执行一个不需要返回具体数据的操作
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        if(whiteNames.contains(path)){
            return chain.filter(exchange);
        }

        String userToken = request.getHeaders().getFirst("user_token");
        log.info("从header中获取token:{}",userToken);
        if(!StringUtils.hasLength(userToken)){
            return unauthorizedResponse(exchange,"令牌不能为空");
        }

        Claims claims = JWTUtils.parseJWT(userToken);
        if(claims==null) {
            log.info("令牌验证失败");
            return unauthorizedResponse(exchange, "令牌不合法或者过期");
        }
            return chain.filter(exchange);
    }

    @SneakyThrows
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String errMsg){
        log.error("[用户认证失败], url:{}", exchange.getRequest().getURI().getPath());

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Result result = Result.fail(errMsg);
        DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(result));
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder(){
        return -200;//oder值越小，优先级越高
    }


}
