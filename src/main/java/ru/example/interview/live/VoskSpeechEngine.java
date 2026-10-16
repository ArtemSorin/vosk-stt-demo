package ru.example.interview.live;

import ru.example.vosk.service.SpeechSession;

/** Адаптер: потоковый Vosk-распознаватель → интерфейс SpeechEngine. */
public final class VoskSpeechEngine implements SpeechEngine {

    private final SpeechSession session;

    public VoskSpeechEngine(SpeechSession session) {
        this.session = session;
    }

    @Override
    public Result accept(byte[] pcm, int len) {
        SpeechSession.Event e = session.accept(pcm, len);
        return e == null ? null : new Result("final".equals(e.type()), e.text());
    }

    @Override
    public Result finish() {
        SpeechSession.Event e = session.finish();
        return new Result(true, e.text());
    }

    @Override
    public void close() {
        session.close();
    }
}
