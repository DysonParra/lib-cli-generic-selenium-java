/*
 * @fileoverview    {LogProcessor}
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

import com.project.dev.selenium.generic.struct.Action;
import com.project.dev.selenium.generic.struct.Config;
import com.project.dev.selenium.generic.struct.Element;
import com.project.dev.selenium.generic.struct.Page;
import com.project.dev.selenium.generic.struct.Task;
import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.json.simple.JSONObject;

/**
 * TODO: Description of {@code LogProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class LogProcessor {

    /**
     * TODO: Description of {@code printConfigMap}.
     *
     * @param configMap
     *
     */
    public static void printConfigMap(@NonNull Map<String, Config> configMap) {
        System.out.println("\nConfig:");
        for (String key : configMap.keySet())
            System.out.println(configMap.get(key));
    }

    /**
     * TODO: Description of {@code printJsonData}.
     *
     * @param jsonData
     *
     */
    public static void printJsonData(@NonNull JSONObject jsonData) {
        System.out.println("\nData:");
        for (Object key : jsonData.keySet())
            System.out.println(key + " = " + jsonData.get(key));
    }

    /**
     * TODO: Description of {@code printUrlFileList}.
     *
     * @param urlFileList
     *
     */
    public static void printUrlFileList(@NonNull List<String> urlFileList) {
        System.out.println("\nUrl list:");
        if (urlFileList.isEmpty())
            System.out.println("Empty");
        else
            for (String url : urlFileList)
                System.out.println(url);
    }

    /**
     * TODO: Description of {@code printInputFileList}.
     *
     * @param inputFileList
     *
     */
    public static void printInputFileList(@NonNull List<File> inputFileList) {
        System.out.println("\nFiles:");
        if (inputFileList.isEmpty())
            System.out.println("Empty");
        else
            inputFileList.forEach(file -> System.out.println(file));
    }

    /**
     * TODO: Description of {@code printTaskList}.
     *
     * @param taskList
     *
     */
    public static void printTaskList(@NonNull List<Task> taskList) {
        System.out.println("\nTasks:");
        for (Task task : taskList) {
            System.out.println(task);
            for (Page page : task.getPages()) {
                System.out.println("    " + page);
                for (Element element : page.getElements()) {
                    System.out.println("        " + element);
                    for (Action action : element.getActions())
                        System.out.println("            " + action);
                }
            }
        }
    }

    /**
     * TODO: Description of {@code printIterableUrlList}.
     *
     * @param iterableUrlList
     *
     */
    public static void printIterableUrlList(List<String> iterableUrlList) {
        System.out.println("\nIterable list:");
        for (String url : iterableUrlList)
            System.out.println(url);
    }
}
