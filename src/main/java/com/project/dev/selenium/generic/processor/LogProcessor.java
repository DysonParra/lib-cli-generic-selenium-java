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
import com.project.dev.selenium.generic.struct.ElementRange;
import com.project.dev.selenium.generic.struct.Navigation;
import com.project.dev.selenium.generic.struct.NavigationRange;
import com.project.dev.selenium.generic.struct.element.DomElement;
import com.project.dev.selenium.generic.struct.navigation.Page;
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
     * TODO: Description of {@code printElementListRecursive}.
     *
     * @param elementList
     * @param tab
     *
     */
    private static void printActionList(@NonNull List<Action> elementList, int tab) {
        for (Action current : elementList) {
            for (int i = 0; i < tab; i++)
                System.out.print("    ");
            System.out.println(current);
        }
    }

    /**
     * TODO: Description of {@code printElementListRecursive}.
     *
     * @param elementList
     * @param tab
     *
     */
    private static void printElementListRecursive(@NonNull List<Element> elementList, int tab) {
        for (Element current : elementList) {
            for (int i = 0; i < tab; i++)
                System.out.print("    ");
            System.out.println(current);
            if (current instanceof DomElement) {
                DomElement aux = (DomElement) current;
                int auxTab = tab + 1;
                printActionList(aux.getActions(), auxTab);
            } else if (current instanceof ElementRange) {
                ElementRange range = (ElementRange) current;
                printElementListRecursive(range.getElements(), ++tab);
            }
        }
    }

    /**
     * TODO: Description of {@code printNavigationListRecursive}.
     *
     * @param navigationList
     * @param tab
     *
     */
    private static void printNavigationListRecursive(@NonNull List<Navigation> navigationList, int tab) {
        for (Navigation current : navigationList) {
            for (int i = 0; i < tab; i++)
                System.out.print("    ");
            System.out.println(current);
            if (current instanceof Page) {
                Page aux = (Page) current;
                int auxTab = tab + 1;
                printElementListRecursive(aux.getElements(), auxTab);
            } else if (current instanceof NavigationRange) {
                NavigationRange range = (NavigationRange) current;
                printNavigationListRecursive(range.getNavigation(), ++tab);
            }
        }
    }

    /**
     * TODO: Description of {@code printNavigationList}.
     *
     * @param navigationList
     *
     */
    public static void printNavigationList(@NonNull List<Navigation> navigationList) {
        System.out.println("\nPages:");
        printNavigationListRecursive(navigationList, 0);
    }

    /**
     * TODO: Description of {@code printPageListRecursive}.
     *
     * @param pageList
     * @param tab
     *
     */
    private static void printPageListRecursive(@NonNull List<Page> pageList, int tab) {
        for (Page current : pageList) {
            for (int i = 0; i < tab; i++)
                System.out.print("    ");
            System.out.println(current);
            int auxTab = tab + 1;
            printElementListRecursive(current.getElements(), auxTab);

        }
    }

    /**
     * TODO: Description of {@code printPageList}.
     *
     * @param pageList
     *
     */
    public static void printPageList(@NonNull List<Page> pageList) {
        System.out.println("\nPages:");
        printPageListRecursive(pageList, 0);
    }

}
