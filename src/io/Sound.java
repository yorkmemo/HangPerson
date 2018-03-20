package io;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Sound {
    static int TICKS_PER_SECOND = 15;

    private static Map<String, Audio> soundMap = new HashMap<String, Audio>();
    private static AllAudio all = new AllAudio(soundMap);
    private static Timeline timeline;

    private static void initialize() {
        if (timeline == null) {
            timeline = new Timeline();
            timeline.setCycleCount(Animation.INDEFINITE);

            double duration = Math.round(1000 / TICKS_PER_SECOND);

            timeline.getKeyFrames().add(new KeyFrame(new Duration(duration), e -> tick()));
            timeline.play();
        }
    }

    public static Audable file(String filename) {
        initialize();

        if (!soundMap.containsKey(filename)) {
            soundMap.put(filename, new Audio(filename));
        }

        return soundMap.get(filename);
    }

    private static void tick() {
        for(Map.Entry<String, Audio> entry : soundMap.entrySet()) {
            Audio audio = entry.getValue();
            audio.tick();
        }
    }


    public static Audable all() {
        return all;
    }

    public static void tts(String text) {
        try {
            Runtime.getRuntime().exec("Say " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
