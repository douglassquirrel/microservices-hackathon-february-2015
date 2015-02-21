package fireDetection;

import colisionDetection.BotPositionsMessage;
import colisionDetection.ScoreEventHandler;
import combo.Combo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static combo.ComboFactory.httpCombo;
import static java.net.URI.create;

public class FireDetectionListener {

    public static final String PLAYER_COORDINATES_TOPIC_NAME = "validPlayerCoordinates";
    public static final String SHOTS_TOPIC_NAME = "playerFire";

    private final String url;

    public FireDetectionListener(String url) {
        this.url = url;
    }

    public void start() {
        final Combo combo = httpCombo(create(url));

        FireDetectionService fireDetectionService = new FireDetectionService(new ScoreEventHandler(combo, "hit"));

        System.out.println("--- connected ---");

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                combo.facts(PLAYER_COORDINATES_TOPIC_NAME, BotPositionsMessage.class)
                        .forEach(botPositionMessage -> {
                            fireDetectionService.updateState(botPositionMessage);
                        });
            }
        });
        combo.facts(SHOTS_TOPIC_NAME, PlayerFireMessage.class)
        .forEach(playerFireMessage -> {
            fireDetectionService.fire(playerFireMessage.getId());
        });


        System.out.println("--- end ---");
    }

}
