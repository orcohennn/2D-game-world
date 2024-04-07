package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a block in the game world.
 */
public class Block extends GameObject {

    /**
     * The size of the block. It is a constant (static final).
     */
    public static final int SIZE = 30;

    /**
     * Constructs a Block object with the specified top-left corner and renderable.
     * @param topLeftCorner The position of the top-left corner of the block.
     * @param renderable    The renderable component for the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
