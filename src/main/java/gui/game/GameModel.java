package gui.game;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Robot movement class.
 * Calculates all robot movements and coordinates
 */
public class GameModel{
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;


    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addNewListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setTargetPosition(Point p) {
        support.firePropertyChange("targetPosition", new Point(m_targetPositionX, m_targetPositionY), p);
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    public void updateRobot() {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angleDifference = asNormalizedRadians(angleToTarget - m_robotDirection);
        double angularVelocity = (angleDifference < Math.PI) ? maxAngularVelocity : -maxAngularVelocity;
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        int duration = 10;
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection + angularVelocity * duration) - Math.sin(m_robotDirection));

        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }

        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection + angularVelocity * duration) - Math.cos(m_robotDirection));

        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        support.firePropertyChange("robotPositionX", m_targetPositionX, newX);
        support.firePropertyChange("robotPositionY", m_targetPositionY, newY);
        support.firePropertyChange("robotDirection", m_robotDirection, newDirection);
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        m_robotDirection = newDirection;
    }

    /**
     * Convert angle into normalized radian
     *
     * @param angle - angle
     * @return - normalized radian value
     */
    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    /**
     * Apply given limits on given value
     *
     * @param value - value to be limited
     * @param min   - minimal limit
     * @param max   - maximum limit
     * @return limited value
     */
    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    /**
     * Calculate distance from first point to second
     *
     * @param x1 - first point x
     * @param y1 - first point y
     * @param x2 - second point x
     * @param y2 - second point y
     * @return distance from first point to second
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Calculate angle from first point to second
     *
     * @param fromX - first point x
     * @param fromY - first point y
     * @param toX   - second point x
     * @param toY   - second point y
     * @return angle to second point as normalized radians
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public double getRobotPositionX() {
        return m_robotPositionX;
    }

    public double getRobotPositionY() {
        return m_robotPositionY;
    }

    public double getRobotDirection() {
        return m_robotDirection;
    }

    public int getTargetPositionX() {
        return m_targetPositionX;
    }
    
    public int getTargetPositionY() {
        return m_targetPositionY;
    }
}
