package colisionDetection;

import java.util.Map;

public class BotPosition {

    private String id;

    private int[] coordinates;

    public BotPosition() {
    }

    public BotPosition(String id, int x, int y) {
        this.id = id;
        setCoordinates(new int[] {x, y});
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return String.format("BotPosition: {id: %s, x: %s, y: %s}", id, coordinates[0], coordinates[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotPosition that = (BotPosition) o;

        if (coordinates != null ? !coordinates.equals(that.coordinates) : that.coordinates != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }
}
