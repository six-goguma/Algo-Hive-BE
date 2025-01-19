package com.knu.algo_hive.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSession;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RedisConnectionFactory redisConnectionFactory;

    public WebSocketConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/chat")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Redis 메시지 브로커 설정
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("35.193.204.188")  // Redis 호스트
                .setRelayPort(6379)         // Redis 포트
                .setClientLogin("root")    // Redis 로그인
                .setClientPasscode("123456") // Redis 패스워드
                .setSystemLogin("admin")    // 시스템 사용자
                .setSystemPasscode("admin") // 시스템 사용자 패스워드
                .setVirtualHost("default");

        // 클라이언트에서 보낼 메시지 경로 설정
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public ServletServerContainerFactoryBean servletServerContainer() {
        ServletServerContainerFactoryBean factoryBean = new ServletServerContainerFactoryBean();
        factoryBean.setMaxTextMessageBufferSize(1000000);
        factoryBean.setMaxBinaryMessageBufferSize(1000000);
        return factoryBean;
    }
}
