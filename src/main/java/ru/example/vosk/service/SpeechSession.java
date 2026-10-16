package ru.example.vosk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.IOException;

/**
 * Потоковое распознавание одного говорящего.
 * Принимает куски PCM (16 бит, little-endian, моно) и возвращает:
 *  - "partial" — текущая гипотеза, пока вы ещё говорите (меняется каждые ~100 мс);
 *  - "final"   — зафиксированный текст после паузы в речи.
 */
public class SpeechSession implements AutoCloseable {

    public record Event(String type, String text) {}

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Recognizer recognizer;
    private String lastPartial = "";

    public SpeechSession(Model model, float sampleRate) throws IOException {
        this.recognizer = new Recognizer(model, sampleRate);
    }

    /** @return событие для клиента или null, если ничего нового (оптимизация: не шлём дубли). */
    public Event accept(byte[] data, int len) {
        if (recognizer.acceptWaveForm(data, len)) {
            lastPartial = "";
            String text = read(recognizer.getResult(), "text");
            return text.isEmpty() ? null : new Event("final", text);
        }
        String partial = read(recognizer.getPartialResult(), "partial");
        if (partial.equals(lastPartial)) {
            return null;
        }
        lastPartial = partial;
        return new Event("partial", partial);
    }

    /** Дораспознать остаток после остановки записи. */
    public Event finish() {
        lastPartial = "";
        return new Event("final", read(recognizer.getFinalResult(), "text"));
    }

    private static String read(String json, String field) {
        try {
            return MAPPER.readTree(json).path(field).asText("").trim();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Некорректный JSON от Vosk: " + json, e);
        }
    }

    @Override
    public void close() {
        recognizer.close();
    }
}
