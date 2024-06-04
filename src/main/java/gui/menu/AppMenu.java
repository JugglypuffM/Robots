package gui.menu;

import gui.MainApplicationFrame;
import localization.LocaleManager;
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
        super(LocaleManager.getString(CLASSNAME + ".label"));
        setMnemonic(KeyEvent.VK_T);
        JMenuItem exitItem = new JMenuItem(LocaleManager.getString(CLASSNAME + ".exit"), KeyEvent.VK_S);
        exitItem.addActionListener((event) -> {
            Logger.debug("exit trigger");
            mainframe.dispatchEvent(new WindowEvent(mainframe, WindowEvent.WINDOW_CLOSING));
        });
        add(exitItem);
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(CLASSNAME + ".label"));
        getItem(0).setText(bundle.getString(CLASSNAME + ".exit"));
    }
}
