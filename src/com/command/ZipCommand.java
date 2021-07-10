package com.command;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ZipCommand implements Command {
    /*
    getZipFileManager() - This method prompts the user to enter the full path to an archive and returns a new instance
    of ZipFileManager with the path to the archive passed as a parameter. 
     */
    public ZipFileManager getZipFileManager() throws Exception{
        ConsoleHelper.writeMessage("Enter the full path to the archive file:");
        Path zipPath = Paths.get(ConsoleHelper.readString());
        return new ZipFileManager(zipPath);
    }
}