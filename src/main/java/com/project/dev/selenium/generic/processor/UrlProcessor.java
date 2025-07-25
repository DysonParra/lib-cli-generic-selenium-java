/*
 * @overview        {PageProcessor}
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

import java.io.BufferedWriter;
import java.io.File;
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
 * TODO: Description of {@code PageProcessor}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class UrlProcessor {

    /**
     * Navega por un lista de páginas y ejecuta una {@code Function} o una {@code BiFunction} cada
     * vez que termina de cargar cada una de las páginas. Ejecuta la {@code Function} o
     * {@code BiFunction} que sea diferente a {@code null} (una debe tener el valor de {@code null}
     * y la otra uno diferente a {@code null}).
     *
     * @param <T>                  es el tipo de dato de {@code sharedObject}.
     * @param driver               es el driver del navegador.
     * @param urlList              es la lista de páginas.
     * @param maxLoadPageTries     es la máxima cantidad de veces que intentará cargar la página.
     * @param delayTimeBeforeRetry es la cantidad de tiempo que espera antes de reintentar cargar la
     *                             página.
     * @param loadPageTimeOut      es el máximo tiempo que espera a que la página cargue antes de
     *                             reintenar.
     * @param pageFunction         es la {@code Function} que se ejecuta para cada una de las
     *                             páginas si {@code pageBiFunction} es {@code null}.
     * @param pageBiFunction       es la {@code BiFunction} que se ejecuta para cada una de las
     *                             páginas si {@code pageFunction} es {@code null}.
     * @param sharedObject         es una variable compartida que se pasará como parémetro a
     *                             {@code pageBiFunction} cada vez que se navegue a una nueva página
     *                             (si {@code pageBiFunction} no es igual a {@code null}).
     * @return {@code true} si {@code lineFunction} o {@code lineBiFunction} devuelve {@code true}
     *         en todas las ejecuciones, caso contario {@code false}.
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
                        result = false;
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
     * Navega por un lista de páginas y ejecuta una {@code Function} cada vez que termina de cargar
     * cada una de las páginas.
     *
     * @param driver               es el driver del navegador.
     * @param urlList              es la lista de páginas.
     * @param maxLoadPageTries     es la máxima cantidad de veces que intentará cargar la página.
     * @param delayTimeBeforeRetry es la cantidad de tiempo que espera antes de reintentar cargar la
     *                             página.
     * @param loadPageTimeOut      es el máximo tiempo que espera a que la página cargue antes de
     *                             reintenar.
     * @param pageFunction         es la {@code Function} que se ejecuta para cada una de las
     *                             páginas.
     * @return {@code true} si {@code pageFunction} devuelve {@code true} en todas las ejecuciones,
     *         caso contario {@code false}.
     */
    public static boolean forEachUrl(@NonNull WebDriver driver,
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
     * Navega por un lista de páginas y ejecuta una {@code BiFunction} cada vez que termina de
     * cargar cada una de las páginas.
     *
     * @param <T>                  es el tipo de dato de {@code sharedObject}.
     * @param driver               es el driver del navegador.
     * @param urlList              es la lista de páginas.
     * @param maxLoadPageTries     es la máxima cantidad de veces que intentará cargar la página.
     * @param delayTimeBeforeRetry es la cantidad de tiempo que espera antes de reintentar cargar la
     *                             página.
     * @param loadPageTimeOut      es el máximo tiempo que espera a que la página cargue antes de
     *                             reintenar.
     * @param pageBiFunction       es la {@code BiFunction} que se ejecuta para cada una de las
     *                             páginas.
     * @param sharedObject         es una variable compartida que se pasará como parémetro a
     *                             {@code pageBiFunction} cada vez que se navegue a una nueva
     *                             página.
     * @return {@code true} si {@code pageBiFunction} devuelve {@code true} en todas las
     *         ejecuciones, caso contario {@code false}.
     */
    public static <T> boolean forEachUrl(@NonNull WebDriver driver,
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
     * Guarda el código fuente de la página actual donde está {@code driver} y es un ejemplo de
     * {@code BiFunction} que puede ser usada como parámetro de {@code forEachUrl} para almacenar
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
