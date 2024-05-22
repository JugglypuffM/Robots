package gui.game;

import java.awt.*;

/**
 * Graphics painter interface.
 * Provides ability to load new paint logic at runtime by implementing this interface
 * using loaded methods and substituting old Painter with new
 */
public interface Painter {
    /**
     * Robot drawing method
     * Draws robot on the given position pointing at pointing direction
     * @param g graphics on which robot will be drawn
     * @param robotCenterX x coordinate of the robot
     * @param robotCenterY y coordinate of the robot
     * @param direction direction where robot should be pointing
     */
    void drawRobot(Graphics2D g, int robotCenterX, int robotCenterY, double direction);

    /**
     * Target drawing method
     * Draws target point at given coordinates
     * @param g graphics on which target will be drawn
     * @param x x coordinate of the target
     * @param y y coordinate of the target
     */
    void drawTarget(Graphics2D g, int x, int y);
}
