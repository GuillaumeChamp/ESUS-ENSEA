package com.application.data.generator;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import java.nio.file.attribute.*;
 
/**
 * This Java program demonstrates how to compress a directory in ZIP format.
 *
 * @author www.codejava.net
 */
public class ZipDir extends SimpleFileVisitor<Path> {
 
    private static ZipOutputStream zos;
 
    private final Path sourceDir;
 
    public ZipDir(Path sourceDir) {
        this.sourceDir = sourceDir;
    }
 
    @Override
    public FileVisitResult visitFile(Path file,
            BasicFileAttributes attributes) {
 
        try {
            Path targetFile = sourceDir.relativize(file);
 
            zos.putNextEntry(new ZipEntry(targetFile.toString()));
 
            byte[] bytes = Files.readAllBytes(file);
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
 
        } catch (IOException ignored) {
        }
 
        return FileVisitResult.CONTINUE;
    }

    /**
     * Compress a directory
     * @param directory directory to compress
     * @return a byteArrayInput Stream describing the zipped file
     */
    public static ByteArrayInputStream Compress(File directory) {
        String dirPath = directory.getPath();
        Path sourceDir = Paths.get(dirPath);

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            zos = new ZipOutputStream(stream);

            Files.walkFileTree(sourceDir, new ZipDir(sourceDir));

            zos.close();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (IOException ex) {
            System.err.println("I/O Error: " + ex);
        }
        return null;
    }
}