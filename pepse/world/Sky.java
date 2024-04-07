package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sky in the game world.
 */
public class Sky {

    /**
     * The basic color of the sky. It is a constant (static final).
     */
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * The tag assigned to sky objects. It is a constant (static final).
     */
    private static final String SKY_TAG = "sky";


    /*
     * Private constructor to prevent instantiation of Sky objects.
     */
    private Sky() {}

    /**
     * Creates a sky game object with the specified window dimensions.
     * @param windowDimensions The dimensions of the game window.
     * @return                 The created sky game object.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }
}

