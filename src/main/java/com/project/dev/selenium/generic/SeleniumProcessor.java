/*
 * @fileoverview    {SeleniumProcessor} se encarga de realizar tareas específicas.
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
package com.project.dev.selenium.generic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.NonNull;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * TODO: Definición de {@code SeleniumProcessor}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class SeleniumProcessor {

    /**
     * TODO: Definición de {@code processUrls}.
     *
     * @param <T>
     * @param driver
     * @param urlList
     * @param maxLoadPageTries
     * @param delayTimeBeforeRetry
     * @param loadPageTimeOut
     *
     * @param pageFunction
     * @param pageBiFunction
     * @param sharedObject
     * @return
     */
    private static <T> boolean processUrls(WebDriver driver,
            List<String> urlList,
            int maxLoadPageTries,
            int delayTimeBeforeRetry,
            int loadPageTimeOut,
            Function<WebDriver, Boolean> pageFunction,
            BiFunction<WebDriver, T, Boolean> pageBiFunction, T sharedObject) {
        assert pageFunction == null && pageBiFunction == null : "The two funtions are null";
        boolean result = true;
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(loadPageTimeOut));

        int pageNumber = 0;
        for (String url : urlList) {
            if (result) {
                pageNumber++;
                try {
                    int tries = 1;
                    while (tries <= maxLoadPageTries) {
                        System.out.println("Trying to open page '" + url + "' (Trie " + tries + ")");
                        try {
                            driver.get(url);
                            new WebDriverWait(driver, Duration.ofMillis(loadPageTimeOut))
                                    .until((WebDriver webDriver) -> ((JavascriptExecutor) webDriver)
                                    .executeScript("return document.readyState")
                                    .equals("complete"));
                            break;
                        } catch (Exception e) {
                            tries++;
                            try {
                                Thread.sleep(delayTimeBeforeRetry);
                            } catch (Exception in) {

                            }
                        }
                    }
                    if (tries > maxLoadPageTries) {
                        System.out.println("Could not open page '" + driver.getCurrentUrl() + "'.");
                    } else {
                        if (pageFunction == null)
                            result = pageBiFunction.apply(driver, sharedObject);
                        else
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
     * TODO: Definición de {@code forEachPage}.
     *
     * @param driver
     * @param urlList
     * @param maxLoadPageTries
     * @param delayTimeBeforeRetry
     * @param loadPageTimeOut
     * @param pageFunction
     * @return
     */
    public static boolean forEachPage(@NonNull WebDriver driver,
            @NonNull List<String> urlList,
            int maxLoadPageTries,
            int delayTimeBeforeRetry,
            int loadPageTimeOut,
            @NonNull Function<WebDriver, Boolean> pageFunction) {
        return processUrls(driver, urlList, maxLoadPageTries,
                delayTimeBeforeRetry, loadPageTimeOut,
                pageFunction, null, null);
    }

    /**
     * TODO: Definición de {@code forEachPage}.
     *
     * @param <T>
     * @param driver
     * @param urlList
     * @param maxLoadPageTries
     * @param delayTimeBeforeRetry
     * @param loadPageTimeOut
     * @param pageBiFunction
     * @param sharedObject
     * @return
     */
    public static <T> boolean forEachPage(@NonNull WebDriver driver,
            @NonNull List<String> urlList,
            int maxLoadPageTries,
            int delayTimeBeforeRetry,
            int loadPageTimeOut,
            @NonNull BiFunction<WebDriver, T, Boolean> pageBiFunction,
            @NonNull T sharedObject) {
        return processUrls(driver, urlList, maxLoadPageTries,
                delayTimeBeforeRetry, loadPageTimeOut,
                null, pageBiFunction, sharedObject);
    }

    /**
     * TODO: Definición de {@code getPageSource}.
     *
     * @param driver
     * @param outputPath
     * @return
     */
    public static boolean getPageSource(@NonNull WebDriver driver, @NonNull String outputPath) {
        try {
            try ( BufferedWriter pageWriter = new BufferedWriter(
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

}
