package com.archiver;

import com.command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static final Map<Operation, Command> commandsMap = new HashMap<>();

    static {
        commandsMap.put(Operation.CREATE, new ZipCreateCommand());
        commandsMap.put(Operation.ADD, new ZipAddCommand());
        commandsMap.put(Operation.REMOVE, new ZipRemoveCommand());
        commandsMap.put(Operation.EXTRACT, new ZipExtractCommand());
        commandsMap.put(Operation.CONTENT, new ZipContentCommand());
        commandsMap.put(Operation.EXIT, new ExitCommand());
    }

    private CommandExecutor() {
    }

    /*
    execute() - This method calls the execute method corresponding to the passed operation.
     */
    public static void execute(Operation operation) throws Exception {
        commandsMap.get(operation).execute();
    }
}
