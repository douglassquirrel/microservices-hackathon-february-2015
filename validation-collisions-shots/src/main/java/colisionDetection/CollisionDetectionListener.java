package colisionDetection;

import combo.Combo;

import static combo.ComboFactory.httpCombo;
import static java.net.URI.create;

public class CollisionDetectionListener {

    public static final String TOPIC_NAME = "playerCoordinates";
    public static final String OUT_TOPIC_NAME = "validPlayerCoordinates";

    private final String url;

    public CollisionDetectionListener(String url) {
        this.url = url;
    }

    public void start() {
        final Combo combo = httpCombo(create(url));
        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService(new ScoreEventHandler(combo, "collision"));

        System.out.println("--- connected ---");

        combo.facts(TOPIC_NAME, BotPositionsMessage.class)
                .forEach(botPositionMessage -> {
                    combo.publishFact(OUT_TOPIC_NAME, collisionDetectionService.detectCollisions(botPositionMessage));
                });

        System.out.println("--- end ---");
    }

}
