package gui.menu;

import gui.MainApplicationFrame;

import javax.swing.*;

/**
 * Custom menu bar class
 */
public class MenuBar extends JMenuBar {
    public MenuBar(MainApplicationFrame mainframe) {
        add(new LookAndFeelMenu());
        add(new TestMenu());
        add(new AppMenu(mainframe));
        add(new LocaleMenu(mainframe));
    }
}
