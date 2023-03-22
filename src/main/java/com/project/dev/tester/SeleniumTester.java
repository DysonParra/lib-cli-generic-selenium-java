/*
 * @fileoverview    {SeleniumTester}
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
package com.project.dev.tester;

import com.google.common.collect.ImmutableMap;
import com.project.dev.file.generic.FileProcessor;
import com.project.dev.flag.processor.Flag;
import com.project.dev.flag.processor.FlagMap;
import com.project.dev.selenium.generic.SeleniumProcessor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;

/**
 * TODO: Definición de {@code SeleniumTester}.
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
     * TODO: Definición de {@code processFlags}.
     *
     * @param flags
     * @return
     */
    public static boolean processFlags(Flag[] flags) {
        boolean result;

        Map<String, String> flagsMap = FlagMap.convertFlagsArrayToMap(flags);
        String chromeDriverPath = flagsMap.get("-chromeDriverPath");
        String urlsFilePath = flagsMap.get("-urlsFilePath");
        String outputPath = flagsMap.get("-outputPath");
        String chromeUserDataDir = System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data";
        String chromeProfileDir = flagsMap.get("-chromeProfileDir");

        int maxLoadPageTries = 3;
        int delayTimeBeforeRetry = 2000;
        int loadPageTimeOut = 10000;
        int delayTimeBeforeNextPage = 200;

        chromeUserDataDir = FlagMap.validateFlagInMap(flagsMap, "-chromeUserDataDir", chromeUserDataDir, String.class);
        maxLoadPageTries = FlagMap.validateFlagInMap(flagsMap, "-maxLoadPageTries", maxLoadPageTries, Integer.class);
        delayTimeBeforeRetry = FlagMap.validateFlagInMap(flagsMap, "-delayTimeBeforeRetry", delayTimeBeforeRetry, Integer.class);
        loadPageTimeOut = FlagMap.validateFlagInMap(flagsMap, "-loadPageTimeOut", loadPageTimeOut, Integer.class);
        FlagMap.validateFlagInMap(flagsMap, "-delayTimeBeforeNextPage", delayTimeBeforeNextPage, Integer.class);

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
