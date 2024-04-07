package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

/**
 * Represents a stump object in the game world.
 */
public class Stump extends GameObject implements OnJumpObserver {

    /**
     * The color of the stump. It is a constant (static final).
     */
    private static final Color STUMP_COLOR = new Color(100, 50, 20);

    /**
     * The tag assigned to stump objects. It is a constant (static final).
     */
    private static final String STUMP_TAG = "stump";

    /**
     * Constructs a stump object with the specified parameters.
     * @param bottomPosition The bottom position of the stump.
     * @param stumpHeight    The height of the stump.
     */
    public Stump(Vector2 bottomPosition, int stumpHeight) {
        super(bottomPosition.add(Vector2.UP.mult(stumpHeight)),
                new Vector2(Block.SIZE, stumpHeight), new RectangleRenderable(STUMP_COLOR));
        this.setTag(STUMP_TAG);
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * Handles the jump event.
     */
    @Override
    public void onJump() {
        this.renderer().setRenderable(new RectangleRenderable(ColorSupplier.approximateColor(STUMP_COLOR)));
    }
}

