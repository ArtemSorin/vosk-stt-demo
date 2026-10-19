package ru.example.vosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "ru.example")
@ConfigurationPropertiesScan("ru.example")
public class VoskSttApplication {
    public static void main(String[] args) {
        System.setProperty("jna.encoding", "UTF-8");
        SpringApplication.run(VoskSttApplication.class, args);
    }
}
