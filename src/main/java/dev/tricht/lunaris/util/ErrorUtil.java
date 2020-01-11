package dev.tricht.lunaris.util;

import javax.swing.*;

public class ErrorUtil {
    public static void showErrorDialogAndExit(String message) {
        JOptionPane.showMessageDialog(null, message, "Lunaris", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
