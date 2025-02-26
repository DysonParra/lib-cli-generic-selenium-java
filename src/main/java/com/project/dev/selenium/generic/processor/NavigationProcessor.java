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
import com.project.dev.selenium.generic.struct.Config;
import com.project.dev.selenium.generic.struct.Element;
import com.project.dev.selenium.generic.struct.Navigation;
import com.project.dev.selenium.generic.struct.NavigationRange;
import com.project.dev.selenium.generic.struct.navigation.Page;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * TODO: Description of {@code NavigationProcessor}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class NavigationProcessor {

    public static final String DEFAULT_NAVIGATIONS_PACKAGE = "com.project.dev.selenium.generic.struct.navigation";
    public static Long PAGE_INDEX = 0l;

    /**
     * TODO: Description of method {@code loadNavigationFromJson}.
     *
     * @param jsonInputFile
     * @return
     */
    public static JSONArray loadNavigationFromJson(JSONObject jsonInputFile) {
        JSONArray jsonInput = null;
        try {
            jsonInput = (JSONArray) jsonInputFile.get("navigation");
            System.out.println("Navigation loaded");
        } catch (Exception e) {
            System.out.println("Error getting 'navigation'");
        }
        return jsonInput;
    }

    /**
     * TODO: Description of method {@code parseNavigation}.
     *
     * @param jsonInput
     * @param configMap
     * @param mapper
     * @return
     */
    public static List<Navigation> parseNavigation(JSONArray jsonInput, Map<String, Config> configMap, ObjectMapper mapper) {
        List<Navigation> resultList = new ArrayList<>();
        String recursiveNode = "navigation";
        String childNode = "elements";
        for (Object current : jsonInput) {
            try {
                JSONObject jsonCurrent = (JSONObject) current;
                JSONArray arrayRecursive = (JSONArray) jsonCurrent.get(recursiveNode);
                JSONArray arrayChild = (JSONArray) jsonCurrent.get(childNode);
                String type = (String) jsonCurrent.get("type");
                List<Navigation> listRecursiveAux = null;
                List<Element> listChildAux = null;

                if (arrayRecursive != null) {
                    jsonCurrent.remove(recursiveNode);
                    listRecursiveAux = NavigationProcessor.parseNavigation(arrayRecursive, configMap, mapper);
                    if (listRecursiveAux == null) {
                        resultList = null;
                        break;
                    }
                } else if (arrayChild != null) {
                    jsonCurrent.remove(childNode);
                    listChildAux = ElementProcessor.parseElements(arrayChild, configMap, mapper);
                    if (listChildAux == null) {
                        resultList = null;
                        break;
                    }
                }

                String className = DEFAULT_NAVIGATIONS_PACKAGE + '.';
                String[] classNameAux = type.split("-");
                for (String name : classNameAux)
                    className += name.substring(0, 1).toUpperCase() + name.substring(1, name.length());

                Class currentClass = Class.forName(className);
                Navigation entity = (Navigation) mapper.readValue(current.toString(), currentClass);
                if (configMap != null)
                    ConfigProcessor.setConfigValuesToObject(configMap, entity);

                if (entity instanceof Page)
                    ((Page) entity).setElements(listChildAux);
                else if (entity instanceof NavigationRange)
                    ((NavigationRange) entity).setNavigation(listRecursiveAux);
                resultList.add(entity);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                resultList = null;
                break;
            }
        }
        return resultList;
    }

    /**
     * TODO: Description of method {@code removeNavigationRanges}.
     *
     * @param navigationList
     * @param currentRange
     * @param currentValue
     * @param mapper
     * @return
     *
     */
    private static List<Navigation> removeNavigationRanges(@NonNull List<Navigation> navigationList,
            NavigationRange currentRange, Object currentValue, ObjectMapper mapper) {
        List<Navigation> newList = new ArrayList<>();
        for (Navigation current : navigationList) {
            if (current != null) {
                if (current instanceof NavigationRange) {
                    NavigationRange range = (NavigationRange) current;
                    List listRange = range.getRange();
                    if (listRange == null || listRange.isEmpty()) {
                        System.out.println("\nInvalid range for " + range);
                        newList = null;
                        break;
                    } else
                        for (Object c : listRange) {
                            try {
                                List<Navigation> recursive = removeNavigationRanges(range.getNavigation(), range, c, mapper);
                                if (recursive == null) {
                                    newList = null;
                                    break;
                                } else
                                    newList.addAll(recursive);
                            } catch (Exception e) {
                                e.printStackTrace(System.out);
                            }
                        }
                } else if (current instanceof Page) {
                    try {
                        Page aux = (Page) current;
                        List<Element> auxList = ElementProcessor.removeElementRanges(
                                aux.getElements(), null, null, mapper);
                        if (auxList == null || auxList.isEmpty()) {
                            newList = null;
                            break;
                        }
                        aux.setElements(auxList);
                        if (currentValue != null) {
                            String currentStr = mapper.writeValueAsString(current);
                            currentStr = currentRange.replaceEnvs(currentStr, currentValue);
                            JSONObject inputJson = (JSONObject) new JSONParser().parse(currentStr);
                            String node = "elements";
                            List<Element> elements = ElementProcessor.parseElements(
                                    (JSONArray) inputJson.get(node), null, mapper
                            );
                            inputJson.remove(node);
                            aux = mapper.readValue(inputJson.toString(), Page.class);
                            aux.setElements(elements);
                            if (newList != null)
                                newList.add(aux);
                        } else if (newList != null) {
                            newList.add((Page) aux.clone());
                        }
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                }
            }
        }
        return newList;
    }

    /**
     * TODO: Description of method {@code parsePages}.
     *
     * @param pageList
     * @param jsonInput
     * @param configMap
     * @return
     */
    public static boolean parsePages(@NonNull List<Page> pageList, JSONArray jsonInput, Map<String, Config> configMap) {
        boolean result = true;
        ObjectMapper mapper = new ObjectMapper();
        List<Navigation> list = parseNavigation(jsonInput, configMap, mapper);
        if (list == null) {
            System.out.println("Error parsing navigation json");
            result = false;
        } else {
            //LogProcessor.printNavigationList(list);
            //System.out.println("");
            list = removeNavigationRanges(list, null, null, mapper);
            if (list == null) {
                System.out.println("Error processing ranges");
                result = false;
            } else {
                for (Object c : list)
                    pageList.add((Page) c);
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
