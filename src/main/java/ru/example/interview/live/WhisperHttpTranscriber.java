package ru.example.interview.live;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Клиент локального Whisper-сервера (tools/whisper_server.py).
 * Протокол: POST {url} с телом = сырой PCM (16 кГц, 16 бит, моно); ответ 200 — текст UTF-8.
 */
public final class WhisperHttpTranscriber implements Transcriber {

    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
    private final URI uri;
    private final Duration timeout;

    public WhisperHttpTranscriber(String url, Duration timeout) {
        this.uri = URI.create(url);
        this.timeout = timeout;
    }

    @Override
    public CompletableFuture<String> transcribe(byte[] pcm) {
        HttpRequest req = HttpRequest.newBuilder(uri)
                .timeout(timeout)
                .header("Content-Type", "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(pcm))
                .build();
        return client.sendAsync(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenApply(r -> {
                    if (r.statusCode() != 200) {
                        throw new IllegalStateException("Whisper-сервер вернул HTTP " + r.statusCode());
                    }
                    return r.body().trim();
                });
    }
}
