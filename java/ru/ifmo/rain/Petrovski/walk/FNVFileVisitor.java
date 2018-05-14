package ru.ifmo.rain.Petrovski.walk;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FNVFileVisitor extends SimpleFileVisitor<Path> {

    private static final int BUFFER_SIZE = 1024;
    private static final int INITIAL_HASH = 0x811c9dc5;
    private static final int PRIME = 0x01000193;
    public static final String ZERO_HEX_32BIT = "00000000";

    private BufferedWriter writer;

    public FNVFileVisitor(BufferedWriter writer) {
        this.writer = writer;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        try {
            writer.write(getFNVHash(file) + " " + file.toString() + System.lineSeparator());
        } catch (IOException e) {
            throw new IOException("Error: result of hashing file " + file.toString() + " cannot be written to the output file; " + e.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
        try {
            writer.write(ZERO_HEX_32BIT + " " + file.toString() + System.lineSeparator());
        } catch (IOException ee) {
            throw new IOException("Error: result of hashing  nonexistent file " + file.toString() + " cannot be written to the output file; " + ee.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }

    private static String getFNVHash(Path path) {
        try (BufferedInputStream fileReader = new BufferedInputStream(Files.newInputStream(path))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int currentHash = INITIAL_HASH;
            int cntBytes;
            while ((cntBytes = fileReader.read(buffer)) != -1) {
                for (int i = 0; i < cntBytes; ++i) {
                    currentHash *= PRIME;
                    currentHash ^= (buffer[i] & 0xff);
                }
            }
            return String.format("%08x", currentHash);
        } catch (Exception e) {
            return ZERO_HEX_32BIT;
        }
    }
}
