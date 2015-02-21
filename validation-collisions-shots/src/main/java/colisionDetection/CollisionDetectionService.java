package colisionDetection;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.LinkedList;
import java.util.List;

public class CollisionDetectionService {

    public static final int BOARD_N_EDGE = 19;
    public static final int BOARD_S_EDGE = 0;
    public static final int BOARD_W_EDGE = 0;
    public static final int BOARD_E_EDGE = 19;

    private final ScoreEventHandler scoreEventHandler;

    public CollisionDetectionService(ScoreEventHandler scoreEventHandler) {
        this.scoreEventHandler = scoreEventHandler;
    }

    public BotPositionsMessage detectCollisions(BotPositionsMessage input) {
        System.out.println("----------- received message");
        System.out.println(input);

        List<BotPosition> botPositions = new LinkedList<>();
        MultiValueMap<Coordinates, String> playersOnPosition = new LinkedMultiValueMap<>();
        for (BotPosition botPosition : input.getPositions()) {
            BotPosition validBotPosition = validateInsideArena(botPosition);
            botPositions.add(validBotPosition);
            playersOnPosition.add(new Coordinates(validBotPosition.getCoordinates()), validBotPosition.getId());
        }

        BotPositionsMessage output = new BotPositionsMessage(botPositions);
//        System.out.println("send message");
//        System.out.println(output);

        detectCollisions(playersOnPosition);

        return output;
    }

    private void detectCollisions(MultiValueMap<Coordinates, String> playersOnPosition) {
        for (List<String> players : playersOnPosition.values()) {
            if (players.size() > 1) {
                System.out.println("------ players "+players+" collided");
                for (String player : players) {
                    scoreEventHandler.handleScoreEvent(player);
                }
            }
        }
    }

    private BotPosition validateInsideArena(BotPosition botPosition) {
        int validatedX = botPosition.getCoordinates()[0];
        int validatedY = botPosition.getCoordinates()[1];

        if (validatedX < BOARD_W_EDGE) {
            validatedX = BOARD_W_EDGE;
        } else if (validatedX > BOARD_E_EDGE) {
            validatedX = BOARD_E_EDGE;
        }

        if (validatedY < BOARD_S_EDGE) {
            validatedY = BOARD_S_EDGE;
        } else if (validatedY > BOARD_N_EDGE) {
            validatedY = BOARD_N_EDGE;
        }

        return new BotPosition(botPosition.getId(), validatedX, validatedY);
    }

}
