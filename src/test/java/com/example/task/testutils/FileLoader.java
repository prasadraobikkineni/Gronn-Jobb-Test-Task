package com.example.task.testutils;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class FileLoader {

    private FileLoader() {
    }

    public static String read(String filePath) throws IOException {
        return new String(Files.readAllBytes(ResourceUtils.getFile(filePath).toPath()), Charset.defaultCharset());
    }
}
