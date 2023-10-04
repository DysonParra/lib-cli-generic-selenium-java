/*
 * @fileoverview    {FileProcessor}
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
     * Verifica si un archivo existe.
     *
     * @param path es la ruta del archivo.
     * @return {@code true} si el archivo existe, caso contrario {@code false}.
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
     * Verifica si un directorio existe.
     *
     * @param path es la ruta del directorio.
     * @return {@code true} si el directorio existe, caso contrario {@code false}.
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
     * Lee el contenido de un archivo y ejecuta una {@code Function} o una {@code BiFunction} luego
     * de leer cada línea del archivo. Ejecuta la {@code Function} o {@code BiFunction} que sea
     * diferente a {@code null} (una debe tener el valor de {@code null} y la otra uno diferente a
     * {@code null}).
     *
     * @param <T>            es el tipo de dato de {@code sharedObject}.
     * @param path           es la ruta del archivo.
     * @param lineFunction   es la {@code Function} que se ejecutará para cada línea del archivo si
     *                       {@code lineBiFunction} es {@code null}.
     * @param lineBiFunction es la {@code BiFunction} que se ejecutará para cada línea del archivo si
     *                       {@code lineFunction} es {@code null}.
     * @param sharedObject   es una variable compartida que se pasará como parémetro a
     *                       {@code lineBiFunction} cada vez que se lea una línea del archivo (si
     *                       {@code lineBiFunction} no es igual a {@code null}).
     * @return {@code true} si {@code lineFunction} o {@code lineBiFunction} devuelve {@code true}
     *         en todas las líneas del archivo, caso contario {@code false}.
     */
    private static <T> boolean processFile(String path,
            Function<String, Boolean> lineFunction,
            BiFunction<String, T, Boolean> lineBiFunction, T sharedObject) {
        assert lineFunction == null && lineBiFunction == null : "The two funtions are null";
        boolean result = true;
        if (validateFile(path)) {
            try (FileInputStream fis = new FileInputStream(path);
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
     * Lee el contenido de un archivo y ejecuta una {@code Function} cada vez que lee una línea.
     *
     * @param path         es la ruta del archivo.
     * @param lineFunction es la {@code Function} que se ejecutará para cada línea del archivo.
     * @return {@code true} si {@code lineFunction} devuelve {@code true} en todas las líneas del
     *         archivo, caso contario {@code false}.
     */
    public static boolean forEachLine(@NonNull String path,
            @NonNull Function<String, Boolean> lineFunction) {
        return processFile(path, lineFunction, null, null);
    }

    /**
     * Lee el contenido de un archivo y ejecuta una {@code BiFunction} cada vez que lee una línea.
     *
     * @param <T>            es el tipo de dato de {@code sharedObject}.
     * @param path           es la ruta del archivo.
     * @param lineBiFunction es la {@code BiFunction} que se ejecutará para cada línea del archivo.
     * @param sharedObject   es una variable compartida que se pasará como parémetro a
     *                       {@code lineBiFunction} cada vez que se lea una línea del archivo.
     * @return {@code true} si {@code lineBiFunction} devuelve {@code true} en todas las líneas del
     *         archivo, caso contario {@code false}.
     */
    public static <T> boolean forEachLine(@NonNull String path,
            @NonNull BiFunction<String, T, Boolean> lineBiFunction,
            @NonNull T sharedObject) {
        return processFile(path, null, lineBiFunction, sharedObject);
    }

    /**
     * Muestra el contenido de un {@code String} y es un ejemplo de {@code Function} que puede ser
     * usada como parámetro de {@code forEachLine} para mostar cada línea del archivo.
     *
     * @param line es un {@code String} que contiene una línea de un archivo.
     * @return {@code true}.
     */
    public static boolean printLine(@NonNull String line) {
        System.out.println(line);
        return true;
    }

    /**
     * Almacena un {@code String} en un {@code List} y es un ejemplo de {@code BiFunction} que puede
     * ser usada como parámetro de {@code forEachLine} para almacenar cada línea del archivo en una
     * lista.
     *
     * @param line es un {@code String} que contiene una línea de un archivo.
     * @param list es la lista donde se guardará el {@code line}.
     * @return {@code true}.
     */
    public static boolean addLineToList(@NonNull String line, @NonNull List<String> list) {
        list.add(line);
        return true;
    }

    /**
     * Obtiene todos los archivos en la ruta {@code path} que contengan alguno de los formatos
     * indicados por {@code extensions} y los almacena en {@code output}.
     *
     * @param path       es la ruta donde se buscarán los archivos.
     * @param extensions las extensiones de archivo que se almacenarán.
     * @param output     es donde se guardarán los archivos encontrados.
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
