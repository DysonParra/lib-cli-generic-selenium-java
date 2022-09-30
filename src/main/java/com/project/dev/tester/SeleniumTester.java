/*
 * @fileoverview {CrunchyProcessor}, se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @history v1.0 --- La implementacion de {CrunchyProcessor} fue realizada el 14/08/2022.
 * @dev - La primera version de {CrunchyProcessor} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.tester;

import com.google.common.collect.ImmutableMap;
import com.project.dev.flag.processor.Flag;
import com.project.dev.generic.processor.FileProcessor;
import com.project.dev.generic.processor.SeleniumProcessor;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;

/**
 * TODO: Definición de {@code CrunchyProcessor}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class SeleniumTester {

    /**
     * TODO: Definición de {@code addCrunchyUrlToList}.
     *
     * @param line
     * @param list
     * @return
     */
    private static boolean addUrlsToList(String line, List<String> list) {
        if (line.matches("(http://|https://).*?"))
            list.add(line);
        return true;
    }

    /**
     * TODO: Definición de {@code validateFlagInMap}.
     *
     * @param <T>
     * @param flagsMap
     * @param flagName
     * @param defaultValue
     * @param classType
     * @return
     */
    public static <T> T validateFlagInMap(Map<String, String> flagsMap,
            String flagName, T defaultValue, Class<T> classType) {
        boolean validFlag = false;
        T resultValue = null;
        String flagValue = flagsMap.get(flagName);
        if (flagValue != null) {
            try {
                resultValue = classType.getConstructor(String.class).newInstance(flagValue);
                validFlag = true;
            } catch (Exception e) {
                System.out.printf("Invalid value '%s' for flag '%s'.\n", flagValue, flagName);
                //e.printStackTrace(System.out);
            }
        }
        if (!validFlag) {
            try {
                resultValue = defaultValue;
                String defaultFlag = String.valueOf(defaultValue);
                System.out.printf("Using default value '%s' in flag '%s'.\n", defaultFlag, flagName);
                flagsMap.put(flagName, defaultFlag);
            } catch (Exception e) {
                //e.printStackTrace(System.out);
            }
        }
        return resultValue;
    }

    /**
     * TODO: Definición de {@code processFlags}.
     *
     * @param flags
     * @return
     */
    public static boolean processFlags(Flag[] flags) {
        boolean result;

        Map<String, String> flagsMap = new HashMap<>();
        for (Flag aux : flags)
            flagsMap.put(aux.getName(), aux.getValue() == null ? "" : aux.getValue());
        String chromeDriverPath = flagsMap.get("-chromeDriverPath");
        String urlsFilePath = flagsMap.get("-urlsFilePath");
        String outputPath = flagsMap.get("-outputPath");
        String chromeUserDataDir = System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data";
        String chromeProfileDir = flagsMap.get("-chromeProfileDir");

        int maxLoadPageTries = 3;
        int delayTimeBeforeRetry = 2000;
        int loadPageTimeOut = 10000;
        int delayTimeBeforeNextPage = 200;

        chromeUserDataDir = validateFlagInMap(flagsMap, "-chromeUserDataDir", chromeUserDataDir, String.class);
        maxLoadPageTries = validateFlagInMap(flagsMap, "-maxLoadPageTries", maxLoadPageTries, Integer.class);
        delayTimeBeforeRetry = validateFlagInMap(flagsMap, "-delayTimeBeforeRetry", delayTimeBeforeRetry, Integer.class);
        loadPageTimeOut = validateFlagInMap(flagsMap, "-loadPageTimeOut", loadPageTimeOut, Integer.class);
        validateFlagInMap(flagsMap, "-delayTimeBeforeNextPage", delayTimeBeforeNextPage, Integer.class);

        if (!FileProcessor.validateFile(chromeDriverPath)) {
            System.out.println("Invalid file in flag '-chromeDriverPath'");
            result = false;
        } else if (!FileProcessor.validateFile(urlsFilePath)) {
            System.out.println("Invalid file in flag '-urlsFilePath'");
            result = false;
        } else if (!FileProcessor.validatePath(outputPath) && !new File(outputPath).mkdirs()) {
            System.out.println("Invalid path in flag '-outputPath'");
            result = false;
        } else if (!FileProcessor.validatePath(chromeUserDataDir)) {
            System.out.println("Invalid path in flag '-chromeUserDataDir'");
            result = false;
        } else {
            List<String> urls = new ArrayList<>();
            result = FileProcessor.forEachLine(urlsFilePath, SeleniumTester::addUrlsToList, urls);

            System.out.println("\nUrls:");
            urls.forEach(url -> System.out.println(url));
            System.out.println("");

            if (!urls.isEmpty()) {
                System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                ChromeOptions options = new ChromeOptions();
                options.addArguments("user-data-dir=" + chromeUserDataDir);
                if (flagsMap.get("--notUseIncognito") == null)
                    options.addArguments("--incognito");
                if (chromeProfileDir != null)
                    options.addArguments("--profile-directory=" + chromeProfileDir);

                ChromeDriver driver = new ChromeDriver(options);
                DevTools devTools = driver.getDevTools();
                devTools.createSession();
                devTools.send(new Command<>("Network.enable", ImmutableMap.of()));

                result = SeleniumProcessor.forEachPage(driver, urls, maxLoadPageTries,
                        delayTimeBeforeRetry, loadPageTimeOut, SeleniumProcessor::getPageSource,
                        flagsMap.get("-outputPath"));

                System.out.println("Finish processing pages...");
                driver.quit();
            }
        }
        return result;
    }

}