package gui.menu;

import gui.MainApplicationFrame;
import localization.LocaleManager;
import localization.Localizable;
import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Application menu class
 */
public class AppMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "appMenu";

    public AppMenu(MainApplicationFrame mainframe) {
        super("");
        setText(ResourceBundle.getBundle("localization", Locale.getDefault()).getString(String.format("%s.label", CLASSNAME)));
        setMnemonic(KeyEvent.VK_T);
        JMenuItem exitItem = new JMenuItem(ResourceBundle.getBundle("localization", Locale.getDefault()).getString(String.format("%s.exit", CLASSNAME)), KeyEvent.VK_S);
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
