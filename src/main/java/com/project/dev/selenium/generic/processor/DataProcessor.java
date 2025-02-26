/*
 * @fileoverview    {DataProcessor}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementation done.
 * @version 2.0     Documentation added.
 */
package com.project.dev.selenium.generic.processor;

import java.io.FileReader;
import java.util.Iterator;
import lombok.NonNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * TODO: Description of {@code DataProcessor}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class DataProcessor {

    /**
     * TODO: Description of method {@code loadDataFromFile}.
     *
     * @param dataFilePath
     * @return
     */
    public static JSONObject loadDataFromFile(@NonNull String dataFilePath) {
        JSONParser parser = new JSONParser();
        JSONObject jsonData = null;
        try {
            jsonData = (JSONObject) parser.parse(new FileReader(dataFilePath));
            System.out.println("File: '" + dataFilePath + "' success readed.");
            System.out.println("Data loaded");
        } catch (Exception e) {
            System.out.println("Error reading the file: '" + dataFilePath + "'");
        }
        return jsonData;
    }

    /**
     * TODO: Description of method {@code replaceData}.
     *
     * @param jsonData
     * @param field
     * @return
     */
    public static String replaceData(@NonNull JSONObject jsonData, String field) {
        if (field != null) {
            for (Iterator iterator = jsonData.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                field = field.replaceAll(
                        "<" + key + ">", ((String) jsonData.get(key)).replaceAll("\\\\", "\\\\\\\\"));
            }
        }
        return field;
    }

}
