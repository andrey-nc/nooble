package com.ghost.utility;

/**
 * Validates OS type from System properties
 */
public class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static OSType getOSType() {
        if (isLinux()) return OSType.LIN;
        if (isMac()) return OSType.MAC;
        if (isSolaris()) return OSType.SUN;
        if (isWindows()) return OSType.WIN;
        if (isUnix()) return OSType.UNIX;
        return OSType.UNKNOWN;
    }

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isLinux() {
        return OS.contains("nux");
    }

    public static boolean isSolaris() {
        return OS.contains("sun");
    }

    public static boolean isUnix() {
        return     OS.contains("nix")
                || OS.contains("nux")
                || OS.contains("aix")
                || OS.contains("sun")
                || OS.contains("mac");
    }
}
