package gui.menu;

import gui.MainApplicationFrame;
import localization.LocaleManager;
import localization.Localizable;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Localization menu class
 */
public class LocaleMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "localeMenu";

    public LocaleMenu(Container desktopContainer, JMenuBar menuBar) {
        super(LocaleManager.getString(CLASSNAME + ".label"));
        setMnemonic(KeyEvent.VK_A);
        try{
            JDesktopPane desktopPane = (JDesktopPane) desktopContainer;
            JMenuItem russian = new JMenuItem(LocaleManager.getString(CLASSNAME + ".russian"));
            russian.addActionListener((event) ->
                    LocaleManager.getInstance().changeLocale(new Locale("ru"), menuBar, desktopPane));
            add(russian);

            JMenuItem english = new JMenuItem(LocaleManager.getString(CLASSNAME + ".transliteration"));
            english.addActionListener((event) ->
                    LocaleManager.getInstance().changeLocale(new Locale("en"), menuBar, desktopPane));
            add(english);
        } catch (ClassCastException e) {
            Logger.error("Failed to set up localization buttons with message: " + e.getMessage());
        }
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(CLASSNAME + ".label"));
        getItem(0).setText(bundle.getString(CLASSNAME + ".russian"));
        getItem(1).setText(bundle.getString(CLASSNAME + ".transliteration"));

    }
}
