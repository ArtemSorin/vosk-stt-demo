package ru.example.vosk.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import ru.example.interview.web.InterviewWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SttWebSocketHandler sttHandler;
    private final InterviewWebSocketHandler interviewHandler;

    public WebSocketConfig(SttWebSocketHandler sttHandler, InterviewWebSocketHandler interviewHandler) {
        this.sttHandler = sttHandler;
        this.interviewHandler = interviewHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(sttHandler, "/ws/stt");              // демо: речь → текст
        registry.addHandler(interviewHandler, "/ws/interview");  // собеседование в реальном времени
    }

    @Bean
    public ServletServerContainerFactoryBean webSocketContainer() {
        ServletServerContainerFactoryBean c = new ServletServerContainerFactoryBean();
        c.setMaxBinaryMessageBufferSize(64 * 1024);
        c.setMaxTextMessageBufferSize(8 * 1024);
        return c;
    }
}
