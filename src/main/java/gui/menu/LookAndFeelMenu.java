package gui.menu;

import gui.MainApplicationFrame;
import localization.LocaleManager;
import localization.Localizable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

/**
 * Look and feel menu class
 */
public class LookAndFeelMenu extends JMenu implements Localizable {
    private final static String CLASSNAME = "lookAndFeelMenu";

    public LookAndFeelMenu() {
        super(LocaleManager.getString(CLASSNAME + ".label"));
        setMnemonic(KeyEvent.VK_V);
        JMenuItem systemLookAndFeel =
                new JMenuItem(LocaleManager.getString(CLASSNAME + ".systemScheme"), KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        add(systemLookAndFeel);
        JMenuItem crossPlatformLookAndFeel =
                new JMenuItem(LocaleManager.getString(CLASSNAME + ".universalScheme"), KeyEvent.VK_S);
        crossPlatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        add(crossPlatformLookAndFeel);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setText(bundle.getString(CLASSNAME + ".label"));
        getItem(0).setText(bundle.getString(CLASSNAME + ".systemScheme"));
        getItem(1).setText(bundle.getString(CLASSNAME + ".universalScheme"));
    }
}
