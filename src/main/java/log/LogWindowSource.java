package log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */
public class LogWindowSource {
    private int m_iQueueLength;

    private final ConcurrentCircularArray m_messages;
    private final ArrayList<WeakReference<LogChangeListener>> m_listeners;
    private volatile WeakReference<LogChangeListener>[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ConcurrentCircularArray(iQueueLength);
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(new WeakReference<>(listener));
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.removeIf(weakReference -> weakReference.get() == listener);
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        WeakReference<LogChangeListener>[] activeListeners = m_activeListeners;
        if (activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    activeListeners = m_listeners.toArray(new WeakReference[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (WeakReference<LogChangeListener> reference : activeListeners) {
            LogChangeListener listener = reference.get();
            if (listener != null) {
                listener.onLogChanged();
            } else {
                m_listeners.remove(reference);
                m_activeListeners = null;
            }
        }
    }

    public int size() {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_messages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return () -> m_messages.subArrayIterator(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return m_messages;
    }
}
