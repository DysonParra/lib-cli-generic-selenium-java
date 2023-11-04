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
import com.project.dev.selenium.generic.struct.Page;
import com.project.dev.selenium.generic.struct.Task;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;

/**
 * TODO: Description of {@code SettingsProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class SettingsProcessor {

    private static int taskId = 0;
    private static int currentIndex = 0;
    private static Map<String, String> flagsMap;
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String DEFAULT_ACTIONS_PACKAGE = "com.project.dev.selenium.generic.struct.action";

    /**
     * TODO: Description of {@code addUrlToList}.
     *
     * @param line
     * @param list
     * @return
     */
    private static boolean addUrlToList(String line, List<String> list) {
        if (line.matches("(http://|https://).*?"))
            list.add(line);
        return true;
    }

    /**
     * TODO: Description of {@code runPageActions}.
     *
     * @param driver
     * @param pages
     * @return
     */
    public static boolean runPageActions(@NonNull WebDriver driver, @NonNull List<Page> pages) {
        Page page = pages.get(currentIndex++);
        System.out.println(page);
        for (Element element : page.getElements()) {
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
                    System.out.println("Error executing action in element: " + element);
                    System.out.println("Date:    " + DATETIME_FORMAT.format(new Date()));
                    System.out.println("Element: " + webElm);
                    System.out.println("Message: " + e.getMessage().split("\n")[0]);
                    System.out.println("");
                    return false;
                }
                try {
                    Thread.sleep(action.getDelayTimeBeforeNext());
                } catch (InterruptedException e) {
                    System.out.println("Error executing sleep");
                }
            }
        }

        try {
            Thread.sleep(page.getDelayTimeBeforeNext());
        } catch (InterruptedException e) {
            System.out.println("Error executing sleep");
        }
        return true;
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
        String chromeDriverPath = flagsMap.get("-chromeDriverPath");
        String navigationFilePath = flagsMap.get("-navigationFilePath");
        String dataFilePath = flagsMap.get("-dataFilePath");
        String outputPath = flagsMap.get("-outputPath");
        String inputPath = flagsMap.get("-inputPath");
        String urlsFilePath = flagsMap.get("-urlsFilePath");
        String chromeProfileDir = flagsMap.get("-chromeProfileDir");
        String chromeUserDataDir = System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data";
        chromeUserDataDir = FlagMap.validateFlagInMap(flagsMap, "-chromeUserDataDir", chromeUserDataDir, String.class);

        if (!FileProcessor.validateFile(chromeDriverPath)) {
            System.out.println("Invalid file in flag '-chromeDriverPath'");
            result = false;
        } else if (!FileProcessor.validateFile(navigationFilePath)) {
            System.out.println("Invalid file in flag '-navigationFilePath'");
            result = false;
        } else if (!FileProcessor.validateFile(dataFilePath)) {
            System.out.println("Invalid file in flag '-dataFilePath'");
            result = false;
        } else if (!FileProcessor.validatePath(outputPath)) {
            System.out.println("Invalid path in flag '-outputPath'");
            result = false;
        } else if (!FileProcessor.validatePath(chromeUserDataDir)) {
            System.out.println("Invalid path in flag '-chromeUserDataDir'");
            result = false;
        } else if (urlsFilePath != null && !FileProcessor.validateFile(urlsFilePath)) {
            System.out.println("Invalid path in flag '-urlsFilePath'");
            result = false;
        } else if (inputPath != null && !FileProcessor.validatePath(inputPath)) {
            System.out.println("Invalid path in flag '-inputPath'");
            result = false;
        } else {
            JSONObject jsonData;
            Map<String, Config> configMap;
            List<Page> pageList = new ArrayList<>();
            List<String> urlFileList = new ArrayList<>();
            List<File> inputFileList = new ArrayList<>();
            List<Task> taskList = new ArrayList<>();
            List<String> iterableUrlList = new ArrayList<>();

            System.out.println("\nReading files...");
            JSONObject jsonNavigation = null;
            JSONObject jsonConfig;
            JSONArray jsonPages;
            try {
                jsonNavigation = (JSONObject) new JSONParser().parse(new FileReader(navigationFilePath));
                System.out.println("File: '" + navigationFilePath + "' success readed.");
            } catch (IOException | ParseException e) {
                System.out.println("Error reading the file: '" + navigationFilePath + "'");
            }

            jsonData = DataProcessor.loadDataFromFile(dataFilePath);
            jsonConfig = ConfigProcessor.loadConfigFromJson(jsonNavigation);
            jsonPages = PageProcessor.loadPagesFromJson(jsonNavigation);

            if (jsonConfig == null || jsonPages == null || jsonData == null)
                result = false;
            else {
                LogProcessor.printJsonData(jsonData);

                configMap = ConfigProcessor.initConfigMap();
                ConfigProcessor.setConfigValuesToMap(jsonConfig, configMap);
                LogProcessor.printConfigMap(configMap);

                String startDate = (String) configMap.get("start-date").getCanonicalValue();
                boolean runPageActionsForEachFile = (boolean) configMap.get("run-page-actions-for-each-file").getCanonicalValue();
                List allowedFileExtensions = (List) configMap.get("allowed-file-extensions").getCanonicalValue();

                int loadPageTimeOut = (int) (long) configMap.get("load-page-timeout").getCanonicalValue();
                int maxLoadPageTries = (int) (long) configMap.get("max-load-page-tries").getCanonicalValue();
                int maxActionPageTries = (int) (long) configMap.get("max-action-page-tries").getCanonicalValue();
                int delayTimeBeforeRetry = (int) (long) configMap.get("delay-time-before-retry").getCanonicalValue();
                int delayTimeBeforeEnd = (int) (long) configMap.get("delay-time-before-end").getCanonicalValue();
                
                if (urlsFilePath != null)
                    result = FileProcessor.forEachLine(urlsFilePath, SettingsProcessor::addUrlToList, urlFileList);
                LogProcessor.printUrlFileList(urlFileList);

                if (inputPath != null)
                    FileProcessor.getFiles(new File(inputPath), (String[]) allowedFileExtensions.toArray(String[]::new), inputFileList);
                LogProcessor.printInputFileList(inputFileList);

                if (result) {
                    if (runPageActionsForEachFile && !inputFileList.isEmpty())
                        System.out.println("\nAll actions will be executed for each file (" + inputFileList.size() + " times)");
                    else {
                        System.out.println("\nAll actions will be executed one time.");
                        inputFileList.clear();
                        inputFileList.add(null);
                    }
                    result = PageProcessor.parsePages(pageList, jsonPages, jsonData, urlFileList, configMap);
                }

                if (result) {
                    for (Page page : pageList)
                        iterableUrlList.add(page.getUrl());

                    Task aux = Task.builder().pages(pageList).build();
                    for (File file : inputFileList) {
                        Task currentTask;
                        try {
                            currentTask = (Task) aux.clone();
                            currentTask.setId(taskId++);
                            currentTask.setFile(file);
                            taskList.add(currentTask);
                        } catch (CloneNotSupportedException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    EnvironmentProcessor.replaceEnvsOnTasks(taskList, DEFAULT_ACTIONS_PACKAGE, inputPath, outputPath);
                    LogProcessor.printTaskList(taskList);

                    LogProcessor.printIterableUrlList(iterableUrlList);

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

                    for (Task task : taskList) {
                        currentIndex = 0;
                        for (String url : iterableUrlList) {
                            if (!result)
                                break;
                            for (int i = 1; i <= maxActionPageTries; i++) {
                                if (UrlProcessor.forEachPage(driver, Arrays.asList(url), maxLoadPageTries,
                                        delayTimeBeforeRetry, loadPageTimeOut, SettingsProcessor::runPageActions, task.getPages())) {
                                    break;
                                }
                                currentIndex--;
                                if (i == maxActionPageTries) {
                                    result = false;
                                    System.out.println("Error executing actions on page: " + url + "\n");
                                    break;
                                }

                                try {
                                    Thread.sleep(delayTimeBeforeRetry);
                                } catch (InterruptedException e) {
                                    System.out.println("Error executing sleep");
                                }
                                System.out.println("");
                            }
                        }
                    }
                    System.out.println("Finish processing pages...");

                    try {
                        Thread.sleep(delayTimeBeforeEnd);
                    } catch (InterruptedException e) {
                        System.out.println("Error executing sleep");
                    }
                    driver.quit();
                }
            }
        }
        return result;
    }

}
