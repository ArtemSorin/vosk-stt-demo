package ru.example.vosk.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import ru.example.vosk.service.SpeechSession;
import ru.example.vosk.service.SpeechSessionFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Протокол:
 *  клиент → сервер: binary-кадры с PCM 16 кГц / 16 бит / моно; text "eof" — конец записи
 *  сервер → клиент: text JSON {"type":"partial"|"final","text":"..."}
 */
@Component
public class SttWebSocketHandler extends AbstractWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(SttWebSocketHandler.class);

    private final SpeechSessionFactory factory;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, SpeechSession> sessions = new ConcurrentHashMap<>();

    public SttWebSocketHandler(SpeechSessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), factory.create());
        log.info("Микрофон подключён: {}", session.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        SpeechSession speech = sessions.get(session.getId());
        if (speech == null) return;

        ByteBuffer buf = message.getPayload();
        byte[] data = new byte[buf.remaining()];
        buf.get(data);

        SpeechSession.Event event = speech.accept(data, data.length);
        if (event != null) {
            send(session, event);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        if ("eof".equals(message.getPayload().trim())) {
            SpeechSession speech = sessions.get(session.getId());
            if (speech != null) {
                send(session, speech.finish());
            }
            session.close(CloseStatus.NORMAL);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("Ошибка WebSocket {}: {}", session.getId(), exception.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        SpeechSession speech = sessions.remove(session.getId());
        if (speech != null) {
            speech.close();
        }
        log.info("Микрофон отключён: {}", session.getId());
    }

    private void send(WebSocketSession session, SpeechSession.Event event) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(event)));
        }
    }
}
