/*
 * @fileoverview    {NavigationProcessor}
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dev.selenium.generic.struct.Action;
import com.project.dev.selenium.generic.struct.Config;
import com.project.dev.selenium.generic.struct.Element;
import com.project.dev.selenium.generic.struct.Page;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * TODO: Description of {@code NavigationProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class NavigationProcessor {

    public static Long PAGE_INDEX = 0l;

    /**
     * TODO: Description of {@code loadPagesFromJson}.
     *
     * @param jsonNavigation
     * @return
     */
    public static JSONArray loadPagesFromJson(JSONObject jsonNavigation) {
        JSONArray jsonPages = null;
        try {
            jsonPages = (JSONArray) jsonNavigation.get("pages");
            System.out.println("Pages loaded");
        } catch (Exception e) {
            System.out.println("Error getting 'pages'");
        }
        return jsonPages;
    }

    /**
     * TODO: Description of {@code parsePages}.
     *
     * @param pageList
     * @param jsonPages
     * @param jsonData
     * @param urlFileList
     * @param configMap
     * @return
     */
    public static boolean parsePages(@NonNull List<Page> pageList, JSONArray jsonPages, JSONObject jsonData, List<String> urlFileList, Map<String, Config> configMap) {
        boolean result = true;
        ObjectMapper mapper = new ObjectMapper();
        for (Object currentPage : jsonPages) {
            Page page = null;
            List<Element> elementsArray = new ArrayList<>();
            JSONArray elements = null;
            try {
                JSONObject jsonCurrentPage = (JSONObject) currentPage;
                elements = (JSONArray) jsonCurrentPage.get("elements");
                jsonCurrentPage.remove("elements");
                page = mapper.readValue(jsonCurrentPage.toJSONString(), Page.class);
                page.setId(PAGE_INDEX++);
                ConfigProcessor.setConfigValuesToPage(configMap, page);
                if (page.getUrl() == null)
                    throw new Exception("Page can't be null");
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Error getting info of current page");
                System.out.println(e.getMessage());
                result = false;
            }
            if (result && page != null && elements != null) {
                for (Object currentElement : elements) {
                    if (!result)
                        break;
                    try {
                        JSONObject jsonCurrentElement = (JSONObject) currentElement;
                        Element element = Element.builder()
                                .id(DataProcessor.replaceData(jsonData, (String) jsonCurrentElement.get("id")))
                                .name(DataProcessor.replaceData(jsonData, (String) jsonCurrentElement.get("name")))
                                .xpath(DataProcessor.replaceData(jsonData, (String) jsonCurrentElement.get("xpath")))
                                .build();
                        List<Action> actionList = new ArrayList<>();
                        for (Object currentAction : (JSONArray) jsonCurrentElement.get("actions")) {
                            JSONObject jsonCurrentAction = (JSONObject) currentAction;
                            String type = DataProcessor.replaceData(jsonData, (String) jsonCurrentAction.get("type"));
                            for (Iterator iterator = jsonCurrentAction.keySet().iterator(); iterator.hasNext();) {
                                String key = (String) iterator.next();
                                Object value = jsonCurrentAction.get(key);
                                if (value instanceof String)
                                    jsonCurrentAction.put(key, DataProcessor.replaceData(jsonData, (String) value));
                                else if (value instanceof List) {
                                    List list = (List) value;
                                    for (int i = 0; i < list.size(); i++) {
                                        Object elm = list.get(i);
                                        if (elm instanceof String) {
                                            list.remove(i);
                                            list.add(i, DataProcessor.replaceData(jsonData, (String) elm));
                                        }
                                    }
                                }
                            }
                            String className = SettingsProcessor.DEFAULT_ACTIONS_PACKAGE + '.';
                            String[] classNameAux = type.split("-");
                            for (String name : classNameAux)
                                className += name.substring(0, 1).toUpperCase() + name.substring(1, name.length());

                            Class actionClass = Class.forName(className);
                            Action action = (Action) mapper.readValue(currentAction.toString(), actionClass);
                            result = EnvironmentProcessor.replaceEnvsOnActionNavigate(urlFileList, actionList, action);
                            if (!result)
                                break;
                        }
                        element.setActions(actionList);
                        elementsArray.add(element);
                    } catch (Exception e) {
                        System.out.println("Error parsing element:");
                        System.out.println(currentElement);
                        //e.printStackTrace();
                        result = false;
                        break;
                    }
                }
                page.setElements(elementsArray);
                result = EnvironmentProcessor.replaceEnvsOnPages(urlFileList, pageList, page);
                if (!result)
                    break;
            }
        }
        return result;
    }

    /**
     * Navega por un lista de páginas y ejecuta una {@code Function} o una {@code BiFunction} cada
     * vez que termina de cargar cada una de las páginas.Ejecuta la {@code Function} o
     * {@code BiFunction} que sea diferente a {@code null} (una debe tener el valor de {@code null}
     * y la otra uno diferente a {@code null}).
     *
     * @param <T>            es el tipo de dato de {@code sharedObject}.
     * @param driver         es el driver del navegador.
     * @param pageList       es la lista de páginas.
     * @param pageFunction   es la {@code Function} que se ejecuta para cada una de las páginas si
     *                       {@code pageBiFunction} es {@code null}.
     * @param pageBiFunction es la {@code BiFunction} que se ejecuta para cada una de las páginas si
     *                       {@code pageFunction} es {@code null}.
     * @param sharedObject   es una variable compartida que se pasará como parémetro a
     *                       {@code pageBiFunction} cada vez que se navegue a una nueva página (si
     *                       {@code pageBiFunction} no es igual a {@code null}).
     * @return {@code true} si {@code lineFunction} o {@code lineBiFunction} devuelve {@code true}
     *         en todas las ejecuciones, caso contario {@code false}.
     */
    private static <T> boolean processUrls(WebDriver driver,
            List<Page> pageList,
            Function<WebDriver, Boolean> pageFunction,
            BiFunction<WebDriver, T, Boolean> pageBiFunction, T sharedObject) {
        assert pageFunction == null && pageBiFunction == null : "The two funtions are null";
        boolean result = true;
        int pageNumber = 0;
        for (Page page : pageList) {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(page.getLoadPageTimeOut()));
            if (result) {
                pageNumber++;
                try {
                    int tries = 1;
                    while (tries <= page.getMaxLoadPageTries()) {
                        System.out.println("Trying to open page '" + page.getUrl() + "' (Trie " + tries + ")");
                        try {
                            driver.get(page.getUrl());
                            new WebDriverWait(driver, Duration.ofMillis(page.getLoadPageTimeOut()))
                                    .until((WebDriver webDriver) -> ((JavascriptExecutor) webDriver)
                                    .executeScript("return document.readyState")
                                    .equals("complete"));
                            break;
                        } catch (Exception e) {
                            tries++;
                            try {
                                Thread.sleep(page.getDelayTimeBeforeRetry());
                            } catch (Exception in) {
                                System.out.println(in.getMessage());
                            }
                        }
                    }
                    if (tries > page.getMaxLoadPageTries()) {
                        System.out.println("Could not open page '" + driver.getCurrentUrl() + "'.");
                        result = false;
                    } else {
                        if (pageFunction == null && pageBiFunction != null)
                            result = pageBiFunction.apply(driver, sharedObject);
                        else if (pageFunction != null)
                            result = pageFunction.apply(driver);
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                    result = false;
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Navega por un lista de páginas y ejecuta una {@code Function} cada vez que termina de cargar
     * cada una de las páginas.
     *
     * @param driver       es el driver del navegador.
     * @param pageList     es la lista de páginas.
     * @param pageFunction es la {@code Function} que se ejecuta para cada una de las páginas.
     * @return {@code true} si {@code pageFunction} devuelve {@code true} en todas las ejecuciones,
     *         caso contario {@code false}.
     */
    public static boolean forEachPage(@NonNull WebDriver driver,
            @NonNull List<Page> pageList,
            @NonNull Function<WebDriver, Boolean> pageFunction) {
        return processUrls(driver, pageList, pageFunction, null, null);
    }

    /**
     * Navega por un lista de páginas y ejecuta una {@code BiFunction} cada vez que termina de
     * cargar cada una de las páginas.
     *
     * @param <T>            es el tipo de dato de {@code sharedObject}.
     * @param driver         es el driver del navegador.
     * @param pageList       es la lista de páginas.
     * @param pageBiFunction es la {@code BiFunction} que se ejecuta para cada una de las páginas.
     * @param sharedObject   es una variable compartida que se pasará como parémetro a
     *                       {@code pageBiFunction} cada vez que se navegue a una nueva página.
     * @return {@code true} si {@code pageBiFunction} devuelve {@code true} en todas las
     *         ejecuciones, caso contario {@code false}.
     */
    public static <T> boolean forEachPage(@NonNull WebDriver driver,
            @NonNull List<Page> pageList,
            @NonNull BiFunction<WebDriver, T, Boolean> pageBiFunction,
            @NonNull T sharedObject) {
        return processUrls(driver, pageList, null, pageBiFunction, sharedObject);
    }

    /**
     * Guarda el código fuente de la página actual donde está {@code driver} y es un ejemplo de
     * {@code BiFunction} que puede ser usada como parámetro de {@code forEachPage} para almacenar
     * guardar el código fuente de cada una de las páginas de la lista.
     *
     * @param driver     es el driver del navegador.
     * @param outputPath es el directorio donde se va a guardar el código fuente de la página.
     * @return {@code true}.
     */
    public static boolean getPageSource(@NonNull WebDriver driver, @NonNull String outputPath) {
        try {
            try (BufferedWriter pageWriter = new BufferedWriter(
                    new FileWriter(
                            new StringBuilder()
                                    .append(outputPath)
                                    .append("/")
                                    .append(driver.getTitle().replaceAll("\\\\|:|/|\\*|\\?|\"|<|>|\\|", ""))
                                    .append(".html")
                                    .toString()))) {
                pageWriter.write(driver.getPageSource());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Guarda el código fuente de la página actual donde está {@code driver} en la ruta del archivo
     * indicada por {@code outputFile}.
     *
     * @param driver     es el driver del navegador.
     * @param outputFile es el archivo donde se va a guardar el código fuente de la página.
     * @return {@code true}.
     */
    public static boolean getPageSource(@NonNull WebDriver driver, @NonNull File outputFile) {
        try {
            try (BufferedWriter pageWriter = new BufferedWriter(
                    new FileWriter(
                            outputFile))) {
                pageWriter.write(driver.getPageSource());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
