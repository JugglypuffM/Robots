package gui.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameVisualizer extends JPanel implements PropertyChangeListener {
    private final GameModel model;
    private final GameController m_controller;
    private Painter painter = new MyPainter();

    public GameVisualizer(GameModel model) {
        this.model = model;
        m_controller = new GameController(model);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_controller.setTargetPosition(e.getPoint());
            }
        });
        setDoubleBuffered(true);
        model.addNewListener(this);
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    public void changePainter(Painter painter) {
        this.painter = painter;
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        painter.drawRobot(g2d, round(model.getRobotPositionX()),
                round(model.getRobotPositionY()),
                model.getRobotDirection());
        painter.drawTarget(g2d, model.getTargetPositionX(), model.getTargetPositionY());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        EventQueue.invokeLater(this::repaint);
    }
}
