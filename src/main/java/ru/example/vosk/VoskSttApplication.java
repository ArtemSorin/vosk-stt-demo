package ru.example.vosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

// Сканируем весь ru.example: и демо распознавания (ru.example.vosk), и собеседование (ru.example.interview)
@SpringBootApplication(scanBasePackages = "ru.example")
@ConfigurationPropertiesScan("ru.example")
public class VoskSttApplication {
    public static void main(String[] args) {
        // Vosk отдаёт строки в UTF-8, а JNA по умолчанию читает их в кодировке ОС (на русской Windows это cp1251).
        // Свойство должно быть задано ДО первого обращения к JNA/Vosk.
        System.setProperty("jna.encoding", "UTF-8");
        SpringApplication.run(VoskSttApplication.class, args);
    }
}
