package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
/**
 * Represents the sun in the game world.
 */
public class Sun {

    /**
     * The initial angle of the sun. It is a constant (static final).
     */
    private static final float INITIAL_SUN_ANGLE = 0f;

    /**
     * The final angle of the sun. It is a constant (static final).
     */
    private static final float FINAL_SUN_ANGLE = 360f;

    /**
     * The size of the sun. It is a constant (static final).
     */
    private static final float SUN_SIZE = 100;

    /**
     * The factor for determining the center of the sun. It is a constant (static final).
     */
    private static final float HALF_FACTOR = 0.5f;

    /**
     * The default terrain height factor. It is a constant (static final).
     */
    private static final float DEFAULT_TERRAIN_HEIGHT_FACTOR = (float) 2 / 3;

    /**
     * The tag for the sun GameObject. It is a constant (static final).
     */
    private static final String SUN_TAG = "sun";

    /**
     * The height factor for the sun. It is a constant (static final).
     */
    private static final float SUN_HEIGHT_FACTOR = (float) 1 / 3;

    /*
    Private constructor to prevent instantiation
    */
    private Sun() {}

    /**
     * Creates a GameObject representing the sun.
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The length of the day-night cycle.
     * @return The GameObject representing the sun.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        Renderable sunRenderable = new OvalRenderable(Color.YELLOW);
        Vector2 sunSize = Vector2.ONES.mult(SUN_SIZE);
        GameObject sun = new GameObject(Vector2.ZERO, sunSize, sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        setCycleTransition(windowDimensions, cycleLength, sun);
        return sun;
    }

    /*
     * Sets the transition for the sun during the day-night cycle.
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The length of the day-night cycle.
     * @param sun              The sun GameObject.
     */
    private static void setCycleTransition(Vector2 windowDimensions, float cycleLength, GameObject sun) {
        Vector2 cycleCenter = new Vector2(windowDimensions.x() * HALF_FACTOR,
                windowDimensions.y() * DEFAULT_TERRAIN_HEIGHT_FACTOR);
        sun.setCenter(new Vector2(windowDimensions.x() * HALF_FACTOR,
                windowDimensions.y() * SUN_HEIGHT_FACTOR));
        Vector2 initialSunCenter = sun.getCenter();
        new Transition<>(sun, (Float angle) ->
                sun.setCenter(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                INITIAL_SUN_ANGLE, FINAL_SUN_ANGLE, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
    }
}
