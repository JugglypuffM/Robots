package gui.menu;

import gui.MainApplicationFrame;
import localization.LocaleManager;
import localization.Localizable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Localization menu class
 */
public class LocaleMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "localeMenu";

    public LocaleMenu(LocaleManager localeManager) {
        super("");
        setMnemonic(KeyEvent.VK_A);
        JMenuItem russian = new JMenuItem("");
        russian.addActionListener((event) -> localeManager.changeLocale(new Locale("ru")));
        add(russian);

        JMenuItem english = new JMenuItem("");
        english.addActionListener((event) -> localeManager.changeLocale(new Locale("en")));
        add(english);
        localeChange(LocaleManager.getBundle());
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(String.format("%s.label", CLASSNAME)));
        getItem(0).setText(bundle.getString(String.format("%s.russian", CLASSNAME)));
        getItem(1).setText(bundle.getString(String.format("%s.transliteration", CLASSNAME)));

    }
}
