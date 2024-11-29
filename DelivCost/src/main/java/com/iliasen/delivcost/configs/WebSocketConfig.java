//package com.iliasen.delivcost.configs;
//
//import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableWebSocketMassageBrocker
//public class WebSocketConfig
//        implements WebSocketMessageBrockerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic/", "/queue/");
//        config.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/greeting");
//    }
//}