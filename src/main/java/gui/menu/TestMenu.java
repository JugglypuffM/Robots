package gui.menu;

import gui.MainApplicationFrame;
import localization.LocaleManager;
import localization.Localizable;
import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

/**
 * Test menu
 */
public class TestMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "testMenu";

    public TestMenu() {
        super(LocaleManager.getString(CLASSNAME + ".label"));
        setMnemonic(KeyEvent.VK_T);
        JMenuItem addLogMessageItem =
                new JMenuItem(LocaleManager.getString(CLASSNAME + ".testMessage"), KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
        add(addLogMessageItem);
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(CLASSNAME + ".label"));
        getItem(0).setText(bundle.getString(CLASSNAME + ".testMessage"));
    }
}
