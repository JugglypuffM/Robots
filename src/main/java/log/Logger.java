package log;

import java.util.Arrays;

public final class Logger {
    private static final LogWindowSource defaultLogSource;

    static {
        defaultLogSource = new LogWindowSource(5);
    }

    private Logger() {
    }

    private static String appendStacktrace(String strMessage, StackTraceElement[] stackTrace){
        return strMessage
                + "\nStacktrace:\n"
                + Arrays.toString(stackTrace).replace(",", ",\n");
    }

    public static void debug(String strMessage) {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }
    public static void debug(String strMessage, StackTraceElement[] stackTrace) {
        defaultLogSource.append(LogLevel.Debug, appendStacktrace(strMessage, stackTrace));
    }

    public static void error(String strMessage) {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }
    public static void error(String strMessage, StackTraceElement[] stackTrace) {
        defaultLogSource.append(LogLevel.Error, appendStacktrace(strMessage, stackTrace));
    }


    public static LogWindowSource getDefaultLogSource() {
        return defaultLogSource;
    }
}
