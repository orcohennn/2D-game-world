package pepse.world.daynight;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;


/**
 * Represents the night environment in the game world.
 */
public class Night {

    // Constants
    /**
     * The opacity value at midnight. It is a constant (static final).
     */
    private static final float MIDNIGHT_OPACITY = 0.5f;

    /**
     * The fully transparent opacity value. It is a constant (static final).
     */
    private static final float FULLY_TRANSPARENT = 0f;

    /**
     * The tag for the night GameObject. It is a constant (static final).
     */
    private static final String NIGHT_TAG = "night";

    /**
     * The factor for determining the center of the GameObject. It is a constant (static final).
     */
    private static final float HALF_FACTOR = 0.5f;

    /*
    Private constructor to prevent instantiation
    */
    private Night() {}

    /**
     * Creates a GameObject representing the night environment.
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The length of the day-night cycle.
     * @return The GameObject representing the night environment.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        Renderable nightRenderable = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO,windowDimensions,nightRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<>( night, night.renderer()::setOpaqueness,
                FULLY_TRANSPARENT,
                MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT,cycleLength * HALF_FACTOR,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        return night;
    }
}

