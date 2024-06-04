package localization;

import java.util.ResourceBundle;

/**
 * Localizable object interface
 */
public interface Localizable {
    /**
     * Change localization within object
     *
     * @param bundle bundle with localized object fields
     */
    public void localeChange(ResourceBundle bundle);
}
