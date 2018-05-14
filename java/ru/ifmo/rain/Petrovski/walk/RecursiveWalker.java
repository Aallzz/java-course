package ru.ifmo.rain.Petrovski.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecursiveWalker {

    private Path inputFile;
    private Path outputFile;

    private void initializeFiles(String fileName, String type) throws  RecursiveWalkerException {
        try {
            if (type.equals("input")) {
                inputFile = Paths.get(fileName);
            } else {
                outputFile = Paths.get(fileName);
            }
        } catch (InvalidPathException e) {
            throw new RecursiveWalkerException("Error: Invalid " + type + " file name;  " + e.getMessage());
        }
    }

    public RecursiveWalker(String inputFileName, String outputFileName) throws RecursiveWalkerException {
        if (inputFileName == null || outputFileName == null) {
            throw new RecursiveWalkerException("Error: Null file name");
        }
        initializeFiles(inputFileName, "input");
        initializeFiles(outputFileName, "output");
    }

    public void walk() throws RecursiveWalkerException {
        try (BufferedReader inputFileReader = Files.newBufferedReader(inputFile)) {
            try (BufferedWriter outputFileWriter = Files.newBufferedWriter(outputFile)) {
                FNVFileVisitor visitor = new FNVFileVisitor(outputFileWriter);
                try {
                    String fileName;
                    while ((fileName = inputFileReader.readLine()) != null) {
                        try {
                            Files.walkFileTree(Paths.get(fileName), visitor);
                        } catch (InvalidPathException e) {
                            outputFileWriter.write(FNVFileVisitor.ZERO_HEX_32BIT + " " + fileName);
                        } catch (IOException e) {
                            throw error("Error: Recursive file tree walk wasn't ended successfully ", e);
                        }
                    }
                } catch (IOException e) {
                    throw error("Error: Problems during reading from input file " + inputFile.toString(), e);
                }
            } catch (IOException e) {
                throw error("Error: Output file " + outputFile + " open failed, or it is impossible to create", e);
            } catch (SecurityException e)  {
                throw new RecursiveWalkerException("Error: Accessing to output file " + outputFile.toString() + " is not allowed; " + e.getMessage(), e);
            }
        } catch (IOException e) {
            throw new RecursiveWalkerException("Error: Input file " + inputFile.toString() + " open failed; " + e.getMessage(), e);
        } catch (SecurityException e)  {
            throw new RecursiveWalkerException("Error: Accessing to input file " + inputFile.toString() + " is not allowed; " + e.getMessage(), e);
        }
    }

    private RecursiveWalkerException error(String message, IOException e) {
        return new RecursiveWalkerException(message + "; " + e.getMessage(), e);
    }
}
