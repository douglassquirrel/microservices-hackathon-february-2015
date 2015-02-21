package colisionDetection;

import java.util.LinkedList;
import java.util.List;

public class BotPositionsMessage {

    private List<BotPosition> positions = new LinkedList<>();

    public BotPositionsMessage() {
    }

    public BotPositionsMessage(List<BotPosition> positions) {
        this.positions = positions;
    }

    public List<BotPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<BotPosition> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BotPositionMessage: [");
        if (!positions.isEmpty()) {
            sb.append("\n");
            for (BotPosition position : positions) {
                sb.append("\t").append(position.toString()).append("\n");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
