package gui.menu;

import gui.JarLoader;
import gui.MainApplicationFrame;
import gui.game.GameModel;
import gui.game.GameVisualizer;
import localization.LocaleManager;
import localization.Localizable;
import log.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RobotLoadMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "robotLoadMenu";
    private final JarLoader loader = new JarLoader();

    public RobotLoadMenu(MainApplicationFrame mainframe) {
        super("");
        setMnemonic(KeyEvent.VK_T);
        JMenuItem addLogMessageItem = new JMenuItem("", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("JAR files", "jar"));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try{
                    Class<?> modelClass = null;
                    Class<?> visualizerClass = null;
                    loader.loadClass(file, modelClass, visualizerClass);
                    if(modelClass != null && visualizerClass != null){
                        GameModel newModel =
                                (GameModel) modelClass
                                        .getDeclaredConstructor(GameModel.class).newInstance();
                        GameVisualizer newVisualizer =
                                (GameVisualizer) visualizerClass
                                        .getDeclaredConstructor(GameVisualizer.class).newInstance(newModel);
                        mainframe.updateRobot(newVisualizer, newModel);
                        Logger.debug("New robot loaded");
                    }
                    else {
                        Logger.error("Robot change failed due to absence of required classes: " +
                                "make sure that your classes have 'model' and 'visualizer' in their names respectively");
                    }
                } catch (IOException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | InvocationTargetException | NoClassDefFoundError e){
                    Logger.error("Robot change failed due to invalid jar-file with message:\n"
                            + e.getMessage()
                            + "\n\n" + "Stacktrace:\n"
                            + Arrays.toString(e.getStackTrace()));

                }
            }
        });
        add(addLogMessageItem);
        localeChange(LocaleManager.getBundle());
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(String.format("%s.label", CLASSNAME)));
        getItem(0).setText(bundle.getString(String.format("%s.buttonMessage", CLASSNAME)));
    }
}
