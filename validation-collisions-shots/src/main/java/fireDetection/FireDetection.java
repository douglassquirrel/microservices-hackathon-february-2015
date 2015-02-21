package fireDetection;

import colisionDetection.CollisionDetectionListener;

public class FireDetection {

    public static void main(final String[] args) {

        FireDetectionListener fdl = new FireDetectionListener("http://52.16.7.112:8000");

        fdl.start();

    }

}
