package com.archiver;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private Path rootPath;
    private List<Path> fileList;

    public FileManager(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        this.fileList = new ArrayList<>();
        collectFileList(rootPath);
    }

    public List<Path> getFileList() {
        return fileList;
    }

    /*
    This method takes a path to a directory and recursively goes through the directory. Using depth first search, the method
    walks down the directory and collects all the regular file paths in the directory into a list.
     */
    private void collectFileList(Path path) throws IOException {
        // Adds files to the fileList
        if (Files.isRegularFile(path)) {
            Path relativePath = rootPath.relativize(path);
            fileList.add(relativePath);
        }

        // Add the contents of a directory
        if (Files.isDirectory(path)) {
            // To avoid writing code to call close on the DirectoryStream, we'll wrap the "new DirectoryStream" call in a try-with-resources
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                for (Path file : directoryStream) {
                    collectFileList(file);
                }
            }
        }
    }
}
