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
 * Singleton class that contains current resource bundle and provides static access to it
 * Also has method to change application locale
 */
public class LocaleManager {
    private ResourceBundle bundle = ResourceBundle.getBundle("localization", Locale.getDefault());
    private static LocaleManager instance;

    private LocaleManager() {}
    public static LocaleManager getInstance() {
        if (instance == null) {
            synchronized (LocaleManager.class) {
                if (instance == null) {
                    instance = new LocaleManager();
                }
            }
        }
        return instance;
    }

    /**
     * Change locale of all components in given {@link JDesktopPane} and {@link JMenuBar} that implement {@link Localizable}
     * @param locale new locale
     * @param menuBar menu bar that will be localized
     * @param desktopPane desktop pane that will be localized
     */
    public void changeLocale(Locale locale, JMenuBar menuBar, JDesktopPane desktopPane) {
        try {
            bundle = ResourceBundle.getBundle("localization", locale);
            for (Component menuItem : menuBar.getComponents())
                if (menuItem instanceof Localizable item)
                    item.localeChange(bundle);
            for (Component component : desktopPane.getComponents())
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

    public ResourceBundle getBundle(){
        return bundle;
    }

    /**
     * Get string from bundle
     * @param messageCode property name
     * @return property value
     */
    public static String getString(String messageCode) {
        return getInstance().getBundle().getString(messageCode);
    }
}
