package com.aerokube.lightning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class FileUtils {

    public static Path zipFile(Path file) {
        try {
            Path zipPath = Files.createTempFile("lightning", "");
            try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                try {
                    ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
                    zs.putNextEntry(zipEntry);
                    Files.copy(file, zs);
                    zs.closeEntry();
                } catch (IOException e) {
                    throw new WebDriverException(e);
                }
            }
            return zipPath;
        } catch (IOException e) {
            throw new WebDriverException(e);
        }
    }

    static List<String> serializeFiles(Path... files) {
        List<String> serializedFiles = new ArrayList<>();
        for (Path file : files) {
            if (!Files.exists(file)) {
                throw new WebDriverException(String.format("Extension does not exist: %s", file.toAbsolutePath()));
            }
            serializedFiles.add(encodeFileToBase64(file));
        }
        return serializedFiles;
    }

    static Path zipDirectory(Path dir) {
        try {
            Path zipPath = Files.createTempFile("lightning", "");
            try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                Files.walk(dir)
                        .filter(path -> !Files.isDirectory(path))
                        .forEach(path -> {
                            try {
                                ZipEntry zipEntry = new ZipEntry(dir.relativize(path).toString());
                                zs.putNextEntry(zipEntry);
                                Files.copy(path, zs);
                                zs.closeEntry();
                            } catch (IOException e) {
                                throw new WebDriverException(e);
                            }
                        });
            }
            return zipPath;
        } catch (IOException e) {
            throw new WebDriverException(e);
        }
    }

    public static String encodeFileToBase64(Path path) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new WebDriverException(e);
        }
    }
}
