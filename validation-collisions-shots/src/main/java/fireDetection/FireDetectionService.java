package fireDetection;

import colisionDetection.BotPosition;
import colisionDetection.BotPositionsMessage;
import colisionDetection.ScoreEventHandler;
import colisionDetection.Coordinates;

import java.util.List;

public class FireDetectionService {

    private List<BotPosition> botPositionList;

    private final ScoreEventHandler scoreEventHandler;

    public FireDetectionService(ScoreEventHandler scoreEventHandler) {
        this.scoreEventHandler = scoreEventHandler;
    }

    public void updateState(BotPositionsMessage botPositionMessage) {
        botPositionList = botPositionMessage.getPositions();
        System.out.println("state updated");
    }

    public void fire(String playerId) {
        System.out.println("---"+playerId + " - shot!");
        Coordinates playerCoordinates = null;

        for (BotPosition botPosition : botPositionList) {
            if (botPosition.getId().equals(playerId)) {
                playerCoordinates = new Coordinates(botPosition.getCoordinates());
            }
        }
        if (playerCoordinates == null) {
            return;
        }

        for (BotPosition botPosition : botPositionList) {
            if (botPosition.getCoordinates()[0] == playerCoordinates.getX()
                    && botPosition.getCoordinates()[1] > playerCoordinates.getY()) {
                System.out.println("player "+botPosition.getId()+" hit");
                scoreEventHandler.handleScoreEvent(botPosition.getId());
            }
        }


    }

}
