package localization;

import gui.MainApplicationFrame;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Locale manager class
 * Has static method to get access to resource bundle
 */
public class LocaleManager {
    private static ResourceBundle bundle = ResourceBundle.getBundle("localization", Locale.getDefault());
    private final MainApplicationFrame mainframe;

    public LocaleManager(MainApplicationFrame mainframe) {
        this.mainframe = mainframe;
    }

    /**
     * Change locale of all application windows
     * @param locale - new locale
     */
    public void changeLocale(Locale locale) {
        try {
            bundle = ResourceBundle.getBundle("localization", locale);
            for (Component menuItem : mainframe.getJMenuBar().getComponents())
                if (menuItem instanceof Localizable item)
                    item.localeChange(bundle);
            for (Component component : mainframe.getDesktopPane().getComponents())
                if (component instanceof Localizable localizable)
                    localizable.localeChange(bundle);
                else if (component instanceof JInternalFrame.JDesktopIcon icon)
                    if (icon.getInternalFrame() instanceof Localizable localizable)
                        localizable.localeChange(bundle);
        } catch (MissingResourceException e) {
            Logger.error("Locale change failed due to missing resources with message:\n" + e.getMessage()
                    + "\n\n" + "Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }
}
