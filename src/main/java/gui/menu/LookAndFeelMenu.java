package gui.menu;

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
        super("Режим отображения");
        setMnemonic(KeyEvent.VK_V);
        getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        add(systemLookAndFeel);
        JMenuItem crossPlatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
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
        setText(bundle.getString(String.format("%s.label", CLASSNAME)));
        getItem(0).setText(bundle.getString(String.format("%s.systemScheme", CLASSNAME)));
        getItem(1).setText(bundle.getString(String.format("%s.universalScheme", CLASSNAME)));
    }
}
