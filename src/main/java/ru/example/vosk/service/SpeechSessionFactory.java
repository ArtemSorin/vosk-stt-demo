package ru.example.vosk.service;

import org.springframework.stereotype.Component;
import ru.example.vosk.config.VoskModelHolder;
import ru.example.vosk.config.VoskProperties;

import java.io.IOException;

@Component
public class SpeechSessionFactory {

    private final VoskModelHolder modelHolder;
    private final VoskProperties props;

    public SpeechSessionFactory(VoskModelHolder modelHolder, VoskProperties props) {
        this.modelHolder = modelHolder;
        this.props = props;
    }

    public SpeechSession create() throws IOException {
        return new SpeechSession(modelHolder.getModel(), props.sampleRate());
    }
}
