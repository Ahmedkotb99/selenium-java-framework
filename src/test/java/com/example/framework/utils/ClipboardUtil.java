package com.example.framework.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public final class ClipboardUtil {
    private static final Duration POLL_INTERVAL = Duration.ofMillis(250);

    private ClipboardUtil() {
    }

    public static void clear() {
        clipboard().setContents(new StringSelection(""), null);
    }

    public static String readText(int timeoutSeconds) {
        Instant deadline = Instant.now().plusSeconds(timeoutSeconds);
        RuntimeException lastException = null;

        while (Instant.now().isBefore(deadline)) {
            try {
                Clipboard clipboard = clipboard();
                if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    String value = (String) clipboard.getData(DataFlavor.stringFlavor);
                    if (value != null && !value.isBlank()) {
                        return value.trim();
                    }
                }
            } catch (IOException | RuntimeException exception) {
                lastException = new RuntimeException(exception);
            } catch (Exception exception) {
                lastException = new RuntimeException(exception);
            }

            sleep();
        }

        throw new IllegalStateException("Clipboard did not contain copied text within "
                + timeoutSeconds + " seconds.", lastException);
    }

    private static Clipboard clipboard() {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    private static void sleep() {
        try {
            Thread.sleep(POLL_INTERVAL.toMillis());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for clipboard text.", exception);
        }
    }
}
