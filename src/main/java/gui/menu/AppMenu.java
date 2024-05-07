package gui.menu;

import gui.MainApplicationFrame;
import localization.Localizable;
import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;


/**
 * Application menu class
 */
public class AppMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "appMenu";

    public AppMenu(MainApplicationFrame mainframe) {
        super("Приложение");
        setMnemonic(KeyEvent.VK_T);
        getAccessibleContext().setAccessibleDescription("Команды приложению");
        JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_S);
        exitItem.addActionListener((event) -> {
            Logger.debug("exit trigger");
            mainframe.dispatchEvent(new WindowEvent(mainframe, WindowEvent.WINDOW_CLOSING));
        });
        add(exitItem);
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(String.format("%s.label", CLASSNAME)));
        getItem(0).setText(bundle.getString(String.format("%s.exit", CLASSNAME)));
    }
}
