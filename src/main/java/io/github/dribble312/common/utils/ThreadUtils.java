package io.github.dribble312.common.utils;

/**
 * @author dribble312
 */
public class ThreadUtils {

    public static String getCurrentMethodName() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        return stackTraceElement.getMethodName();
    }

}
