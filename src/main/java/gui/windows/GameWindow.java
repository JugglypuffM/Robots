package gui.windows;

import gui.game.GameModel;
import gui.game.GameVisualizer;
import localization.LocaleManager;
import localization.Localizable;
import log.Logger;
import save.Memorizable;
import save.StateManager;
import save.WindowInitException;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class GameWindow extends JInternalFrame implements Memorizable, Localizable {
    private final static String CLASSNAME = "gameWindow";
    private final GameVisualizer m_visualizer;

    public GameWindow(StateManager stateManager, GameModel model) {
        super(LocaleManager.getString(CLASSNAME + ".title"), true, true, true, true);
        m_visualizer = new GameVisualizer(model);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        try {
            stateManager.configureFrame(getClassname(), this);
        } catch (WindowInitException e) {
            setSize(400, 400);
            Logger.debug(
                    "Game window initialization failed with message:\n" +
                            e.getMessage() +
                            "\nConfiguring by default"
            );
        }
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
