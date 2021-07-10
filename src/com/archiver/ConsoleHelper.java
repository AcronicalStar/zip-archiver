package com.archiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    /*
    writeMessage() - This method outputs the passed message to the console.
     */
    public static void writeMessage(String message) {
        System.out.println(message);
    }

    /*
    readString() - This method reads Strings that the user has inputted.
     */
    public static String readString() throws IOException {
        String text = bufferedReader.readLine();
        return text;
    }

    /*
    readInt() - This method reads numbers that the user has inputted.
     */
    public static int readInt() throws IOException {
        String text = readString();
        return Integer.parseInt(text.trim());
    }
}
