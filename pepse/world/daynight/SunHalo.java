package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the halo around the sun during daytime.
 */
public class SunHalo {

    /**
     * The color of the halo around the sun. It is a constant (static final).
     */
    private static final Color ALPHA_COLOR = new Color(255, 255, 0, 20);

    /**
     * The factor determining the size of the sun's halo. It is a constant (static final).
     */
    private static final float HALO_SIZE_FACTOR = 2f;

    /**
     * The tag for the sun's halo. It is a constant (static final).
     */
    private static final String SUN_HALO_TAG = "sunHalo";

    /*
        Private constructor to prevent instantiation
     */
    private SunHalo() {}

    /**
     * Creates a GameObject representing the halo around the sun.
     * @param sun The sun GameObject.
     * @return The GameObject representing the sun's halo.
     */
    public static GameObject create(GameObject sun) {
        Renderable sunHaloRenderable = new OvalRenderable(ALPHA_COLOR);
        Vector2 sunHaloSize = sun.getDimensions().mult(HALO_SIZE_FACTOR);
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(), sunHaloSize, sunHaloRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_HALO_TAG);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}
