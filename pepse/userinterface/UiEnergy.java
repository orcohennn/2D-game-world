/**
 * Represents the UI element displaying energy information.
 */
package pepse.userinterface;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;

/**
 * This class represents Ui energy bar that shows the energy on the screen left corner
 */
public class UiEnergy {

    /**
     * The size of the energy text. It is a constant (static final).
     */
    private static final int ENERGY_TEXT_SIZE = 50;

    /**
     * The position of the energy text. It is a constant (static final).
     */
    private static final int ENERGY_TEXT_POSITION = 5;

    /**
     * The unit symbol for percentage. It is a constant (static final).
     */
    private static final String PERCENT = "%";

    /*
    Private constructor to prevent instantiation
    */
    private UiEnergy() {}

    /**
     * Creates a GameObject representing the UI element displaying energy information.
     * @param getEnergySupplier The supplier for obtaining the energy value.
     * @return The GameObject representing the UI element.
     */
    public static GameObject create(Supplier<Double> getEnergySupplier){
        TextRenderable textRenderable = new TextRenderable("");
        Vector2 energyTextSize = Vector2.ONES.mult(ENERGY_TEXT_SIZE);
        GameObject UiEnergy = new GameObject(Vector2.ONES.mult(ENERGY_TEXT_POSITION), energyTextSize,
                textRenderable);
        UiEnergy.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        UiEnergy.addComponent(deltaTime ->
                textRenderable.setString(
                        String.valueOf((int)(double)getEnergySupplier.get()).concat(PERCENT)));
        return UiEnergy;
    }
}

