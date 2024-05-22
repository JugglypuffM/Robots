package gui.menu;

import gui.MainApplicationFrame;
import localization.LocaleManager;
import localization.Localizable;
import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Test menu
 */
public class TestMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "testMenu";

    public TestMenu() {
        super("");
        setText(ResourceBundle.getBundle("localization", Locale.getDefault()).getString(String.format("%s.label", CLASSNAME)));
        setMnemonic(KeyEvent.VK_T);
        JMenuItem addLogMessageItem = new JMenuItem(ResourceBundle.getBundle("localization", Locale.getDefault()).getString(String.format("%s.testMessage", CLASSNAME)), KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
        add(addLogMessageItem);
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(String.format("%s.label", CLASSNAME)));
        getItem(0).setText(bundle.getString(String.format("%s.testMessage", CLASSNAME)));
    }
}
