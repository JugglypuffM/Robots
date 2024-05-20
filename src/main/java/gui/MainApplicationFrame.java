package gui;

import gui.game.GameModel;
import gui.game.GameWindow;
import log.Logger;
import save.Memorizable;
import save.StateManager;
import save.WindowInitException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame implements Memorizable {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final StateManager stateManager = new StateManager();

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

        GameModel model = new GameModel();

        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), stateManager);
        addWindow(logWindow);

        CoordinateWindow coordinateWindow = new CoordinateWindow(stateManager, model);
        addWindow(coordinateWindow);

        GameWindow gameWindow = new GameWindow(stateManager, model);
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
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
        String[] options = {"Да", "Нет"};
        int option = JOptionPane.showOptionDialog(this, "Вы действительно хотите выйти?",
                "Выход", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
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
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        JMenu appMenu = new JMenu("Приложение");
        appMenu.setMnemonic(KeyEvent.VK_T);
        appMenu.getAccessibleContext().setAccessibleDescription(
                "Команды приложению");

        {
            JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_S);
            exitItem.addActionListener((event) -> {
                Logger.debug("exit trigger");
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            });
            appMenu.add(exitItem);
        }

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(appMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    @Override
    public String getClassname() {
        return "mainframe";
    }
}
