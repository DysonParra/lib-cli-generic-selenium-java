/*
 * @fileoverview    {SettingsProcessor}
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

import com.google.common.collect.ImmutableMap;
import com.project.dev.file.generic.FileProcessor;
import com.project.dev.flag.processor.Flag;
import com.project.dev.flag.processor.FlagMap;
import com.project.dev.selenium.generic.struct.Action;
import com.project.dev.selenium.generic.struct.Config;
import com.project.dev.selenium.generic.struct.Element;
import com.project.dev.selenium.generic.struct.element.DomElement;
import com.project.dev.selenium.generic.struct.navigation.Page;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * TODO: Description of {@code SettingsProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class SettingsProcessor {

    private static int currentIndex = 0;
    private static Map<String, String> flagsMap;
    private static final int PAGE_ACTIONS_SUCCESS = -1;
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * TODO: Description of {@code runPageActions}.
     *
     * @param driver
     * @param pages
     * @return
     */
    public static boolean runPageActions(@NonNull WebDriver driver, @NonNull List<Page> pages) {
        boolean result = true;
        boolean elementError = false;
        Page page = pages.get(currentIndex++);
        System.out.println(page);
        for (int i = 1; i <= page.getMaxActionPageTries() + 1; i++) {
            if (i == page.getMaxActionPageTries() + 1) {
                result = false;
                System.out.println("Error executing actions on page: " + page.getUrl());
                break;
            }
            System.out.println("Executing actions (Trie " + i + ")");
            for (Element elm : page.getElements()) {
                DomElement element = (DomElement) elm;
                elementError = false;
                WebElement webElm = null;
                System.out.println(element);
                for (Action action : element.getActions()) {
                    System.out.println("    " + action);
                    try {
                        if (element.getId() != null)
                            webElm = driver.findElement(By.id(element.getId()));
                        else if (element.getName() != null)
                            webElm = driver.findElement(By.name(element.getName()));
                        else
                            webElm = driver.findElement(By.xpath(element.getXpath()));
                        action.executeAction(driver, webElm, flagsMap);
                    } catch (Exception e) {
                        System.out.println("    Error executing action in element: " + element);
                        System.out.println("    Date:    " + DATETIME_FORMAT.format(new Date()));
                        System.out.println("    Element: " + webElm);
                        System.out.println("    Message: " + e.getMessage().split("\n")[0]);
                        elementError = true;
                        break;
                    }
                    try {
                        Thread.sleep(action.getDelayTimeBeforeNext());
                    } catch (InterruptedException e) {
                        System.out.println("Error executing sleep");
                    }
                }
                if (elementError)
                    break;
            }

            if (!elementError)
                i = PAGE_ACTIONS_SUCCESS;

            if (i != PAGE_ACTIONS_SUCCESS && i != page.getMaxActionPageTries()) {
                System.out.println("Reloading...");
                driver.navigate().refresh();
                new WebDriverWait(driver, Duration.ofMillis(page.getLoadPageTimeOut()))
                        .until((WebDriver webDriver) -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
            } else
                break;
        }
        if (result) {
            try {
                Thread.sleep(page.getDelayTimeBeforeNext());
            } catch (InterruptedException e) {
                System.out.println("Error executing sleep");
            }
        }
        System.out.println("");
        return result;
    }

    /**
     * TODO: Description of {@code processFlags}.
     *
     * @param flags
     * @return
     */
    public static boolean processFlags(Flag[] flags) {
        boolean result = true;
        flagsMap = FlagMap.convertFlagsArrayToMap(flags);
        String navigationFilePath = flagsMap.get("-navigationFilePath");
        String dataFilePath = flagsMap.get("-dataFilePath");
        String chromeDriverPath = flagsMap.get("-chromeDriverPath");
        String chromeProfileDir = flagsMap.get("-chromeProfileDir");
        String chromeUserDataDir = System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data";
        chromeUserDataDir = FlagMap.validateFlagInMap(flagsMap, "-chromeUserDataDir", chromeUserDataDir, String.class);

        if (!FileProcessor.validateFile(navigationFilePath)) {
            System.out.println("Invalid file in flag '-navigationFilePath'");
            result = false;
        } else if (!FileProcessor.validateFile(dataFilePath)) {
            System.out.println("Invalid file in flag '-dataFilePath'");
            result = false;
        } else if (!FileProcessor.validateFile(chromeDriverPath)) {
            System.out.println("Invalid file in flag '-chromeDriverPath'");
            result = false;
        } else if (!FileProcessor.validatePath(chromeUserDataDir)) {
            System.out.println("Invalid path in flag '-chromeUserDataDir'");
            result = false;
        } else {
            JSONObject jsonDataFile;
            Map<String, Config> configMap;
            List<Page> pageList = new ArrayList<>();

            System.out.println("\nReading files...");
            JSONObject jsonNavigationFile = null;
            JSONObject jsonConfig;
            JSONArray jsonNavigation;

            jsonDataFile = DataProcessor.loadDataFromFile(dataFilePath);

            try {
                String navigationStr = Files.readString(Paths.get((navigationFilePath)));
                navigationStr = DataProcessor.replaceData(jsonDataFile, navigationStr);
                jsonNavigationFile = (JSONObject) new JSONParser().parse(navigationStr);
                System.out.println("File: '" + navigationFilePath + "' success readed.");
            } catch (IOException | ParseException e) {
                System.out.println("Error reading the file: '" + navigationFilePath + "'");
                System.out.println(e.getCause());
            }

            jsonConfig = ConfigProcessor.loadConfigFromJson(jsonNavigationFile);
            jsonNavigation = NavigationProcessor.loadNavigationFromJson(jsonNavigationFile);

            if (jsonConfig == null || jsonNavigation == null)
                result = false;
            else {
                LogProcessor.printJsonData(jsonDataFile);

                configMap = ConfigProcessor.initConfigMap();
                ConfigProcessor.setConfigValuesToMap(jsonConfig, configMap);
                LogProcessor.printConfigMap(configMap);

                String startDate = (String) configMap.get("start-date").getCanonicalValue();
                int delayTimeBeforeEnd = (int) (long) configMap.get("delay-time-before-end").getCanonicalValue();

                if (result)
                    result = NavigationProcessor.parsePages(pageList, jsonNavigation, configMap);
                if (result)

                    if (result) {
                        LogProcessor.printPageList(pageList);
                        
                        if(configMap.get("only-validate-config-files").getCanonicalValue().equals(true)) {
                            System.out.println("\nOnly validate specified.\n");
                            return true;
                        }

                        ScheduleProcessor.validateAndWait(startDate);
                        
                        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("user-data-dir=" + chromeUserDataDir);
                        options.addArguments("--remote-allow-origins=*");
                        if (flagsMap.get("--notUseIncognito") == null)
                            options.addArguments("--incognito");
                        if (chromeProfileDir != null)
                            options.addArguments("--profile-directory=" + chromeProfileDir);
                        ChromeDriver driver = new ChromeDriver(options);
                        DevTools devTools = driver.getDevTools();
                        devTools.createSession();
                        devTools.send(new Command<>("Network.enable", ImmutableMap.of()));

                        result = NavigationProcessor.forEachPage(driver, pageList,
                                SettingsProcessor::runPageActions, pageList);
                        if (!result)
                            System.out.println("Error executing actions\n");
                        else {
                            System.out.println("Finish processing navigation...");
                            try {
                                Thread.sleep(delayTimeBeforeEnd);
                            } catch (InterruptedException e) {
                                System.out.println("Error executing sleep");
                            }
                        }
                        driver.quit();
                    }
            }
        }
        return result;
    }

}
