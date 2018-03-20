package io;

import java.net.UnknownHostException;

public abstract class Sys {
    private static String temp = null;

    static {
        try {
            temp = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static final String os = System.getProperty("os.name");

    public static final String name = temp;

    public static boolean isMac() {
        return os.toLowerCase().contains("mac");
    }

    public static boolean isWindows() {
        return os.toLowerCase().contains("win");
    }
}
