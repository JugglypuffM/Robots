package gui.windows;

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
    private final JPanel m_visualizer;

    public GameWindow(StateManager stateManager, JPanel visualizer) {
        super(LocaleManager.getString(CLASSNAME + ".title"), true, true, true, true);
        m_visualizer = visualizer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        try {
            stateManager.configureFrame(getClassname(), this);
        } catch (WindowInitException e) {
            setSize(400, 400);
            Logger.error(
                    "Game window initialization failed with message:\n" + e.getMessage(),
                    e.getStackTrace()
            );
            Logger.error("Configuring by default");
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
