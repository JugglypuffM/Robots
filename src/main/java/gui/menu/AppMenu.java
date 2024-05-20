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
        super("");
        setMnemonic(KeyEvent.VK_T);
        JMenuItem exitItem = new JMenuItem("", KeyEvent.VK_S);
        exitItem.addActionListener((event) -> {
            Logger.debug("exit trigger");
            mainframe.dispatchEvent(new WindowEvent(mainframe, WindowEvent.WINDOW_CLOSING));
        });
        add(exitItem);
        localeChange(LocaleManager.getBundle());
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(String.format("%s.label", CLASSNAME)));
        getItem(0).setText(bundle.getString(String.format("%s.exit", CLASSNAME)));
    }
}
