package kib.lab7.server.utils;

import kib.lab7.common.util.console_workers.TextSender;

public final class Config {

    private static boolean isWorking = true;
    private static final TextSender TEXT_SENDER = new TextSender(System.out);

    private Config() {

    }


    public static TextSender getTextSender() {
        return TEXT_SENDER;
    }

    public static boolean isWorking() {
        return isWorking;
    }

    public static void setIsWorking(boolean isWorking) {
        Config.isWorking = isWorking;
    }
}
