package log;

import java.util.Arrays;

public final class Logger {
    private static final LogWindowSource defaultLogSource;

    static {
        defaultLogSource = new LogWindowSource(5);
    }

    private Logger() {
    }

    public static void debug(String strMessage) {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }
    public static void debug(String strMessage, StackTraceElement[] stackTrace) {
        defaultLogSource.append(LogLevel.Debug, strMessage
                + "\n\nStacktrace:\n"
                    + Arrays.toString(stackTrace).replace(",", ",\n")
        );
    }

    public static void error(String strMessage) {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }
    public static void error(String strMessage, StackTraceElement[] stackTrace) {
        defaultLogSource.append(LogLevel.Error, strMessage
                + "\nStacktrace:\n"
                + Arrays.toString(stackTrace).replace(",", ",\n")
        );
    }


    public static LogWindowSource getDefaultLogSource() {
        return defaultLogSource;
    }
}
