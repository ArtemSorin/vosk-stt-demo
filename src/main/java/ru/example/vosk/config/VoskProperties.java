package ru.example.vosk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vosk")
public record VoskProperties(String modelPath, float sampleRate) {
    public VoskProperties {
        if (modelPath == null || modelPath.isBlank()) {
            modelPath = "./models/vosk-model-ru-0.42";
        }
        if (sampleRate <= 0) {
            sampleRate = 16000f;
        }
    }
}
