package gui.windows;

import gui.game.GameModel;
import localization.LocaleManager;
import localization.Localizable;
import log.Logger;
import save.Memorizable;
import save.StateManager;
import save.WindowInitException;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

/**
 * Window that displays robot coordinates
 */
public class CoordinateWindow extends JInternalFrame implements PropertyChangeListener, Memorizable, Localizable {
    private final static String CLASSNAME = "coordinateWindow";
    private final TextArea text = new TextArea();

    public CoordinateWindow(StateManager stateManager, GameModel model) {
        super(LocaleManager.getString(CLASSNAME + ".title"), true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(text, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        try {
            stateManager.configureFrame(getClassname(), this);
        } catch (WindowInitException e) {
            setSize(200, 400);
            Logger.error(
                    "Coordinate window initialization failed with message:\n" +
                            e.getMessage() +
                            "\nConfiguring by default"
            );
        }
        model.addNewListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameModel model = (GameModel) evt.getSource();
        String newLabelText = "targetPositionX = " + model.getTargetPositionX() + "\n" +
                "targetPositionY = " + model.getTargetPositionY() + "\n" +
                "robotPositionX = " + model.getRobotPositionX() + "\n" +
                "robotPositionY = " + model.getRobotPositionY() + "\n" +
                "robotDirection = " + model.getRobotDirection();
        text.setText(newLabelText);
    }

    @Override
    public String getClassname() {
        return CLASSNAME;
    }


    @Override
    public void localeChange(ResourceBundle bundle) {
        setTitle(bundle.getString(CLASSNAME + ".title"));
    }
}
