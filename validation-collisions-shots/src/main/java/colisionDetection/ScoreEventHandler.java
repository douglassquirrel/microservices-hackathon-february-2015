package colisionDetection;

import combo.Combo;

public class ScoreEventHandler {

    public static final String SCORE_EVENT_TOPIC_NAME = "scoreEvent";

    final Combo combo;
    private String eventType;

    public ScoreEventHandler(Combo combo, String eventType) {
        this.combo = combo;
        this.eventType = eventType;
    }

    public void handleScoreEvent(String playerId) {
        combo.publishFact(SCORE_EVENT_TOPIC_NAME, new ScoreEvent(playerId, eventType));
    }

    private static class ScoreEvent {

        private final String id;
        private final String type;

        private ScoreEvent(String id, String type) {
            this.id = id;
            this.type = type;
        }

        private String getId() {
            return id;
        }

        private String getType() {
            return type;
        }
    }

}
