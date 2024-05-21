package gui;

import gui.game.GameModel;
import gui.game.GameVisualizer;
import gui.menu.MenuBar;
import gui.windows.GameWindow;
import gui.windows.CoordinateWindow;
import gui.windows.LogWindow;
import localization.LocaleManager;
import log.Logger;
import save.Memorizable;
import save.StateManager;
import save.WindowInitException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame implements Memorizable {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final StateManager stateManager = new StateManager();
    private final LocaleManager localeManager = new LocaleManager(this);
    private GameModel gameModel;
    private JPanel gameVisualizer;

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        try {
            stateManager.configureFrame(getClassname(), this);
        } catch (WindowInitException e) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(inset, inset,
                    screenSize.width - inset * 2,
                    screenSize.height - inset * 2);
            Logger.debug(
                    "Mainframe initialization failed with message:\n" +
                            e.getMessage() +
                            "\nConfiguring by default"
            );
        }

        setContentPane(desktopPane);

        gameModel = new GameModel();
        gameVisualizer = new GameVisualizer(gameModel);
        GameWindow gameWindow = new GameWindow(stateManager, gameVisualizer);
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), stateManager);
        addWindow(logWindow);

        CoordinateWindow coordinateWindow = new CoordinateWindow(stateManager, gameModel);
        addWindow(coordinateWindow);

        setJMenuBar(new MenuBar(this));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        WindowAdapter listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitOperation();
            }
        };
        addWindowListener(listener);
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Exit operation handler
     * Asks user if he really wants to quit the application
     */
    private void exitOperation() {
        ResourceBundle bundle = localeManager.getBundle();
        String[] options = {bundle.getString("exitDialog.yes"), bundle.getString("exitDialog.no")};
        int option = JOptionPane.showOptionDialog(this, bundle.getString("exitDialog.question"),
                bundle.getString("exitDialog.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);
        if (option == JOptionPane.YES_OPTION) {
            for (Component component : desktopPane.getComponents()) {
                if (component instanceof Memorizable memorizable)
                    stateManager.saveFrame(memorizable.getClassname(), component);
                else if (component instanceof JInternalFrame.JDesktopIcon icon)
                    if (icon.getInternalFrame() instanceof Memorizable memorizable)
                        stateManager.saveFrame(memorizable.getClassname(), component);
            }
            stateManager.saveState();
            Logger.getDefaultLogSource().unregisterAllListeners();
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    public void updateRobot(JPanel visualizer, GameModel model) {
        gameVisualizer = visualizer;
        gameModel = model;
    }

    @Override
    public String getClassname() {
        return "mainframe";
    }
}
