package gui.menu;

import gui.JarClassLoader;
import gui.MainApplicationFrame;
import gui.game.GameVisualizer;
import gui.game.Painter;
import localization.LocaleManager;
import localization.Localizable;
import log.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Robot visualisation change menu
 */
public class RobotLoadMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "robotLoadMenu";
    private final GameVisualizer visualizer;
    private final JarClassLoader loader = new JarClassLoader();

    public RobotLoadMenu(GameVisualizer visualizer) {
        super("");
        setMnemonic(KeyEvent.VK_T);
        JMenuItem addLogMessageItem = new JMenuItem("", KeyEvent.VK_S);
        this.visualizer = visualizer;
        addLogMessageItem.addActionListener((event) -> {
            File file = chooseFile();
            if (file != null) {
                tryToLoadAndUpdatePainter(file);
            }
        });
        add(addLogMessageItem);
        localeChange(LocaleManager.getBundle());
    }

    /**
     * Choose file with explorer interface
     * @return chosen file or null if nothing was chosen
     */
    private File chooseFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("JAR files", "jar"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Try to load class, if not null try to create new implementation of {@link Painter} and update it in the visualizer
     * @param file file with required class
     */
    private void tryToLoadAndUpdatePainter(File file){
        try{
            Class<?> painterClass = loader.loadClass(file, "painter");
            if(painterClass != null){
                Painter newPainter = createPainter(painterClass);
                visualizer.changePainter(newPainter);
                Logger.debug("New robot loaded");
            }
            else {
                Logger.error("Robot change failed due to absence of painter class");
            }
        } catch (IOException | NoSuchMethodException | NoClassDefFoundError | InvocationTargetException |
                 InstantiationException | IllegalAccessException e){
            Logger.error("Robot change failed due to invalid jar-file with message:\n"
                    + e.getMessage()
                    + "\n\n" + "Stacktrace:\n"
                    + Arrays.toString(e.getStackTrace()).replace(",", ",\n"));
            e.printStackTrace();
        }
    }

    /**
     * Create implementation of {@link Painter} with methods from given class
     * @param painterClass loaded class, which should contain required methods
     * @return instance of implemented {@link Painter} interface
     * @throws NoSuchMethodException if accessing to class methods failed
     * @throws InvocationTargetException if {@link java.lang.reflect.Constructor#newInstance} failed
     * @throws InstantiationException if {@link java.lang.reflect.Constructor#newInstance} failed
     * @throws IllegalAccessException if {@link java.lang.reflect.Constructor#newInstance} failed
     */
    private Painter createPainter(Class<?> painterClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object object = painterClass.getDeclaredConstructor().newInstance();
        Method drawRobotMethod = painterClass.getMethod("drawRobot", Graphics2D.class, int.class, int.class, double.class);
        Method drawTargetMethod = painterClass.getMethod("drawTarget", Graphics2D.class, int.class, int.class);
        return new Painter() {
            @Override
            public void drawRobot(Graphics2D g, int robotCenterX, int robotCenterY, double direction) {
                try {
                    drawRobotMethod.invoke(object, g, robotCenterX, robotCenterY, direction);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Logger.error(
                            "Robot paint failed due to exception with message:\n" + e.getMessage(),
                            e.getStackTrace());
                    e.printStackTrace();
                }
            }

            @Override
            public void drawTarget(Graphics2D g, int x, int y) {
                try {
                    drawTargetMethod.invoke(object, g, x, y);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Logger.error(
                            "Target paint failed due to exception with message:\n" + e.getMessage(),
                            e.getStackTrace());
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(String.format("%s.label", CLASSNAME)));
        getItem(0).setText(bundle.getString(String.format("%s.buttonMessage", CLASSNAME)));
    }
}
