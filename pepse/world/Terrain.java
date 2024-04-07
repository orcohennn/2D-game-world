package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the terrain in the game world.
 */
public class Terrain {

    /**
     * The default factor used to determine the height of the terrain. It is a constant (static final).
     */
    private static final float DEFAULT_TERRAIN_HEIGHT_FACTOR = (float) 2 / 3;

    /**
     * The base color of the ground blocks. It is a constant (static final).
     */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    /**
     * The depth of the terrain, representing the number of layers of ground blocks.
     * It is a constant (static final).
     */
    private static final int TERRAIN_DEPTH = 20;

    /**
     * The factor used for noise generation to create terrain variation. It is a constant (static final).
     */
    private static final double NOISE_FACTOR = 7;

    /**
     * The tag assigned to ground blocks. It is a constant (static final).
     */
    private static final String GROUND_TAG = "ground";

    private final NoiseGenerator noiseGenerator;
    private final float groundHeightAtX0;

    /**
     * Constructs a Terrain object with the specified window dimensions and seed.
     * @param windowDimensions  The dimensions of the game window.
     * @param seed              The seed used for noise generation.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = DEFAULT_TERRAIN_HEIGHT_FACTOR * windowDimensions.y();
        this.noiseGenerator = new NoiseGenerator(seed, (int) this.groundHeightAtX0);
    }

    /**
     * Retrieves the height of the ground at the specified x-coordinate.
     * @param x The x-coordinate.
     * @return  The height of the ground at the specified x-coordinate.
     */
    public float groundHeightAt(float x) {
        return (float) this.noiseGenerator.noise(x, NOISE_FACTOR * Block.SIZE) + groundHeightAtX0;
    }

    /**
     * Creates a list of terrain blocks within the specified range.
     * @param minX  The minimum x-coordinate.
     * @param maxX  The maximum x-coordinate.
     * @return      The list of generated terrain blocks.
     */
    public List<Block> createInRange(int minX, int maxX) {
        int startX = (int) (Math.floor((double) minX / Block.SIZE) * Block.SIZE);
        int endX = (int) (Math.floor((double) maxX / Block.SIZE) * Block.SIZE);
        int numOfHorizontalBlocks = (endX - startX) / Block.SIZE;
        List<Block> blockList = new ArrayList<>();
        createBlocks(numOfHorizontalBlocks, startX, blockList);
        return blockList;
    }

    /*
     * Creates terrain blocks within the specified range and adds them to the block list.
     * @param numOfHorizontalBlocks The number of horizontal blocks to create.
     * @param startX                The starting x-coordinate.
     * @param blockList             The list to which the generated blocks are added.
     */
    private void createBlocks(int numOfHorizontalBlocks, int startX, List<Block> blockList) {
        for (int i = 0; i < numOfHorizontalBlocks; i++) {
            int curX = startX + i * Block.SIZE;
            int curY = (int) (Math.floor(groundHeightAt(curX) / Block.SIZE) * Block.SIZE);
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                Renderable blockRenderable = new RectangleRenderable(ColorSupplier
                        .approximateColor(BASE_GROUND_COLOR));
                Block curBlock = new Block(new Vector2(curX, curY), blockRenderable);
                curBlock.setTag(GROUND_TAG);
                blockList.add(curBlock);
                curY += Block.SIZE;
            }
        }
    }

}
