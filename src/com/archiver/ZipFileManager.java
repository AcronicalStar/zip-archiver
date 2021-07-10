package com.archiver;

import com.exception.NoSuchZipFileException;
import com.exception.PathNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private final Path zipFilePath;

    public ZipFileManager(Path zipFile) {
        this.zipFilePath = zipFile;
    }

    /*
    createZip() - This checks creates a new zip archive. A zip output stream is created for the archive and the method
    checks if the path to be archived is a directory or regular file. If the path is a directory, the program
    gets a list of the files inside the directory and adds each of the files to the archive. If the path is to an
    individual file, the file is added to the archive. Otherwise, a PathNotFoundException is thrown
     */
    public void createZip(Path source) throws Exception {
        /*
        Checks if the directory where the archive will be created exists and creates the directory if it does not exist
         */
        Path zipDirectory = zipFilePath.getParent();
        if (Files.notExists(zipDirectory))
            Files.createDirectories(zipDirectory);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            if (Files.isDirectory(source)) {
                // If the source path is a directory, the method gets a list of files it contains
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();

                // Each of the files in the directory are added to the archive
                for (Path fileName : fileNames)
                    addNewZipEntry(zipOutputStream, source, fileName);

            } else if (Files.isRegularFile(source)) {

                // If an individual file is being archived, the method gets the parent directory and file name
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else {

                // If the path is neither a directory or a file, then an exception is thrown
                throw new PathNotFoundException();
            }
        }
    }

    /*
    extractAll() - This method extracts files from an archive into the specified output directory. If the archive file
    exists the method creates a new zip input stream. It subsequently checks if the output directory exists and gets a zip
    entry for each file in the archive. The method resolves the individual file path against the output folder path.
    Finally, the method creates an output stream for the new file path and copies the data from the file in the archive
    to the corresponding file in the output folder
     */
    public void extractAll(Path outputFolder) throws Exception {
        // Checks whether the archive file exists
        if (!Files.isRegularFile(zipFilePath)) {
            throw new NoSuchZipFileException();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFilePath))) {
            // If the output folder does not exist the method creates it here
            if (Files.notExists(outputFolder))
                Files.createDirectories(outputFolder);

            ZipEntry zipEntry = zipInputStream.getNextEntry();

            // The method walks down the directory and gets each individual file
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                Path fileFullName = outputFolder.resolve(fileName);

                Path parent = fileFullName.getParent();
                if (Files.notExists(parent))
                    Files.createDirectories(parent);

                try (OutputStream outputStream = Files.newOutputStream(fileFullName)) {
                    copyData(zipInputStream, outputStream);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    /*
    removeFiles() - This method removes the specified file from an archive. It creates a temp file, a zip output stream
    for the temp file, and a zip input stream for the archive. 
     */
    public void removeFiles(List<Path> pathList) throws Exception {
        // Checks to see if the archive file exists
        if (!Files.isRegularFile(zipFilePath)) {
            throw new NoSuchZipFileException();
        }

        // A temp file is created
        Path tempZipFile = Files.createTempFile(null, null);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFilePath))) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {

                    Path archivedFile = Paths.get(zipEntry.getName());

                    if (!pathList.contains(archivedFile)) {
                        String fileName = zipEntry.getName();
                        zipOutputStream.putNextEntry(new ZipEntry(fileName));

                        copyData(zipInputStream, zipOutputStream);

                        zipOutputStream.closeEntry();
                        zipInputStream.closeEntry();
                    }
                    else {
                        ConsoleHelper.writeMessage(String.format("File '%s' was removed from the archive.", archivedFile.toString()));
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        }

        // Move the temporary file to the location of the original
        Files.move(tempZipFile, zipFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception {
        // Check whether the zip file exists
        if (!Files.isRegularFile(zipFilePath)) {
            throw new NoSuchZipFileException();
        }

        // Create a temporary file
        Path tempZipFile = Files.createTempFile(null, null);
        List<Path> archiveFiles = new ArrayList<>();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFilePath))) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {
                    String fileName = zipEntry.getName();
                    archiveFiles.add(Paths.get(fileName));

                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    copyData(zipInputStream, zipOutputStream);

                    zipInputStream.closeEntry();
                    zipOutputStream.closeEntry();

                    zipEntry = zipInputStream.getNextEntry();
                }
            }

            // Archive new files
            for (Path file : absolutePathList) {
                if (Files.isRegularFile(file))
                {
                    if (archiveFiles.contains(file.getFileName()))
                        ConsoleHelper.writeMessage(String.format("File '%s' already exists in the archive.", file.toString()));
                    else {
                        addNewZipEntry(zipOutputStream, file.getParent(), file.getFileName());
                        ConsoleHelper.writeMessage(String.format("File '%s' was added to the archive.", file.toString()));
                    }
                }
                else
                    throw new PathNotFoundException();
            }
        }

        // Move the temporary file to the location of the original
        Files.move(tempZipFile, zipFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public List<FileProperties> getFileList() throws Exception {
        // Check whether the zip file exists
        if (!Files.isRegularFile(zipFilePath)) {
            throw new NoSuchZipFileException();
        }

        List<FileProperties> files = new ArrayList<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFilePath))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                // The "size" and "compressed size" fields are unknown until the entry is read
                // Let's read it into an output stream
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copyData(zipInputStream, baos);

                FileProperties file = new FileProperties(zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize(), zipEntry.getMethod());
                files.add(file);
                zipEntry = zipInputStream.getNextEntry();
            }
        }

        return files;
    }

    /*
    addNewZipEntry() - This method is a helper method which adds a new zip entry for a file to an zip output stream.
    It resolves a file name to it's parent directory. The method uses try-with-resources to open a new input stream by
    passing in the full path to a file. It then creates a new zip entry for the file, adds the zip entry to the output
    stream, and copies the data inside the file to the output stream
     */
    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        Path fullPath = filePath.resolve(fileName);
        try (InputStream inputStream = Files.newInputStream(fullPath)) {
            ZipEntry entry = new ZipEntry(fileName.toString());

            zipOutputStream.putNextEntry(entry);

            copyData(inputStream, zipOutputStream);

            zipOutputStream.closeEntry();
        }
    }

    /*
    copyData - This method is a helper method that copies the content of an input stream to the passed output stream.
    It reads bytes from the input stream and outputs the contents to
     */
    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] byteArray = new byte[in.available()];
        int length;
        while ((length = in.read(byteArray)) > 0) {
            out.write(byteArray, 0, length);
        }
    }
}