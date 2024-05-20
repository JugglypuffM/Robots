package gui.windows;

import localization.LocaleManager;
import localization.Localizable;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import log.Logger;
import save.Memorizable;
import save.StateManager;
import save.WindowInitException;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class LogWindow extends JInternalFrame implements LogChangeListener, Memorizable, Localizable {
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private final StateManager stateManager;

    public LogWindow(LogWindowSource logSource, StateManager stateManager) {
        super("", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        this.stateManager = stateManager;

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
        try {
            stateManager.configureFrame(getClassname(), this);
        } catch (WindowInitException e) {
            setLocation(10, 10);
            setSize(300, 800);
            setMinimumSize(getSize());
            pack();
            Logger.debug(
                    "Log window initialization failed with message:\n" +
                            e.getMessage() +
                            "\nConfiguring by default"
            );
        }
        localeChange(LocaleManager.getBundle());
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public String getClassname() {
        return "logWindow";
    }

    @Override
    public void localeChange(ResourceBundle bundle) {
        setTitle(bundle.getString(String.format("%s.title", getClassname())));
    }
}
