package io;

import java.util.Map;

public class AllAudio implements Audable {
    private Map<String, Audio> soundMap;

    AllAudio(Map<String, Audio> soundMap) {
        this.soundMap = soundMap;
    }

    @Override
    public Audable play() {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.play();
        }
        return this;
    }

    @Override
    public Audable sleep(double seconds) {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.sleep(seconds);
        }
        return this;
    }

    @Override
    public Audable loop() {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.loop();
        }
        return this;

    }

    @Override
    public Audable loop(int times) {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.loop(times);
        }
        return this;
    }

    @Override
    public Audable stop() {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.stop();
        }
        return this;

    }

    @Override
    public Audable pause() {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.pause();
        }
        return this;
    }

    @Override
    public Audable pause(double seconds) {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.pause(seconds);
        }
        return this;

    }

    @Override
    public Audable onDone(AudioEvent event) {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.onDone(event);
        }
        return this;
    }
}
