package com.archiver;

import com.exception.NoSuchZipFileException;
import java.io.IOException;

public class Archiver {
    public static void main(String[] args) throws IOException {
        Operation operation = null;
        do {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (NoSuchZipFileException e) {
                ConsoleHelper.writeMessage("No archive or invalid file selected.");
            } catch (Exception e) {
                ConsoleHelper.writeMessage("Error. Check the data that was entered.");
            }

        } while (operation != Operation.EXIT);
    }

    /*
    askOperation() - A static method that asks the user to select an operation. The user must enter the number which corresponds to
    the operation. The method returns the selected operation.
     */
    public static Operation askOperation() throws IOException {
        ConsoleHelper.writeMessage("");
        ConsoleHelper.writeMessage("Select an operation:");
        ConsoleHelper.writeMessage(String.format("\t %d - Create an archive and zip files into an archive", Operation.CREATE.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - Add a file to an archive", Operation.ADD.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - Remove a file from an archive", Operation.REMOVE.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - Extract the contents of an  archive", Operation.EXTRACT.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - View the contents of an archive", Operation.CONTENT.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - Exit the archiver", Operation.EXIT.ordinal()));

        return Operation.values()[ConsoleHelper.readInt()];
    }
}
