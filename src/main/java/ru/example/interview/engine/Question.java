package ru.example.interview.engine;

import java.util.List;

public record Question(int id, Level level, String topic, String text, String reference, String notes,
                       List<String> followUps) {
}
