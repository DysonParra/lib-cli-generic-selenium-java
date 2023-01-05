/*
 * @fileoverview    {FileProcessor} se encarga de realizar tareas específicas.
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementación realizada.
 * @version 2.0     Documentación agregada.
 */
package com.project.dev.file.generic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.NonNull;

/**
 * TODO: Definición de {@code FileProcessor}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class FileProcessor {

    /**
     * TODO: Definición de {@code validateFile}.
     *
     * @param path
     * @return
     */
    public static boolean validateFile(String path) {
        try {
            File file;
            file = new File(path);
            if (!file.exists()) {
                System.out.println("File '" + path + "' not found");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * TODO: Definición de {@code validatePath}.
     *
     * @param path
     * @return
     */
    public static boolean validatePath(String path) {
        try {
            File file;
            file = new File(path);
            if (!file.isDirectory()) {
                System.out.println("Directory '" + path + "' not found");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * TODO: Definición de {@code processFile}.
     *
     * @param <T>
     * @param path
     * @param lineFunction
     * @param lineBiFunction
     * @param sharedObject
     * @return
     */
    private static <T> boolean processFile(String path,
            Function<String, Boolean> lineFunction,
            BiFunction<String, T, Boolean> lineBiFunction, T sharedObject) {
        assert lineFunction == null && lineBiFunction == null : "The two funtions are null";
        boolean result = true;
        if (validateFile(path)) {
            try ( FileInputStream fis = new FileInputStream(path);
                     InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                     BufferedReader reader = new BufferedReader(isr);) {
                String line;
                if (lineFunction == null && lineBiFunction != null)
                    while ((line = reader.readLine()) != null && result)
                        result = lineBiFunction.apply(line, sharedObject);
                else if (lineBiFunction == null && lineFunction != null)
                    while ((line = reader.readLine()) != null && result)
                        result = lineFunction.apply(line);

            } catch (IOException e) {
                result = false;
            }
        } else
            result = false;
        return result;
    }

    /**
     * TODO: Definición de {@code forEachLine}.
     *
     * @param path
     * @param lineFunction
     * @return
     */
    public static boolean forEachLine(@NonNull String path,
            @NonNull Function<String, Boolean> lineFunction) {
        return processFile(path, lineFunction, null, null);
    }

    /**
     * TODO: Definición de {@code forEachLine}.
     *
     * @param <T>
     * @param path
     * @param lineBiFunction
     * @param sharedObject
     * @return
     */
    public static <T> boolean forEachLine(@NonNull String path,
            @NonNull BiFunction<String, T, Boolean> lineBiFunction,
            @NonNull T sharedObject) {
        return processFile(path, null, lineBiFunction, sharedObject);
    }

    /**
     * TODO: Definición de {@code printLine}.
     *
     * @param line
     * @return
     */
    public static boolean printLine(@NonNull String line) {
        System.out.println(line);
        return true;
    }

    /**
     * TODO: Definición de {@code addLineToList}.
     *
     * @param line
     * @param list
     * @return
     */
    public static boolean addLineToList(@NonNull String line, @NonNull List<String> list) {
        list.add(line);
        return true;
    }

    /**
     * TODO: Definición de {@code getFiles}.
     *
     * @param path
     * @param extensions
     * @param output
     */
    public static void getFiles(@NonNull File path, @NonNull String[] extensions, @NonNull List<File> output) {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            for (File fp : files)
                getFiles(fp, extensions, output);
        } else {
            String strPath = path.toString();
            if (Arrays.stream(extensions).anyMatch(entry -> strPath.endsWith(entry)))
                output.add(path);
        }
    }
}
