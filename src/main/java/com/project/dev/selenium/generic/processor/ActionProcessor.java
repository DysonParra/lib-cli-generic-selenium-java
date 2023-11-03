/*
 * @fileoverview    {ActionProcessor}
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
import com.google.common.collect.ImmutableMap;
import com.project.dev.file.generic.FileProcessor;
import com.project.dev.flag.processor.Flag;
import com.project.dev.flag.processor.FlagMap;
import com.project.dev.selenium.generic.struct.Action;
import com.project.dev.selenium.generic.struct.Config;
import com.project.dev.selenium.generic.struct.Element;
import com.project.dev.selenium.generic.struct.Page;
import com.project.dev.selenium.generic.struct.Task;
import com.project.dev.selenium.generic.struct.action.Navigate;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;

/**
 * TODO: Description of {@code ActionProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class ActionProcessor {

    private static int taskId = 0;
    private static int currentIndex = 0;
    private static Map<String, String> flagsMap;
    private static Map<String, Config> configMap;
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * TODO: Description of {@code replaceData}.
     *
     * @param jsonData
     * @param field
     * @return
     */
    public static String replaceData(@NonNull JSONObject jsonData, String field) {
        if (field != null) {
            for (Iterator iterator = jsonData.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                field = field.replaceAll("<" + key + ">", (String) jsonData.get(key));
            }
        }
        return field;
    }

    /**
     * TODO: Description of {@code replaceFileEnvs}.
     *
     * @param file
     * @param text
     * @param inputPath
     * @param outputPath
     * @param doubleScape
     * @return
     */
    public static String replaceFileEnvs(File file, String text, String inputPath, String outputPath, boolean doubleScape) {
        String fileFullPathEnv = "%fileFullPath%";
        String filePathEnv = "%filePath%";
        String fileNameEnv = "%fileName%";
        String fileNameNoExtEnv = "%fileNameNoExt%";
        String fileInPathEnv = "%fileInPath%";
        String fileOutPathEnv = "%fileOutPath%";
        String patternIn = "/|\\\\";
        String patternOut = "\\\\\\\\";
        if (doubleScape)
            patternOut += patternOut;
        if (file != null && text != null) {
            text = text.replaceAll(fileFullPathEnv, file.getAbsolutePath().replaceAll(patternIn, patternOut))
                    .replaceAll(filePathEnv, file.getPath().replaceAll(patternIn, patternOut))
                    .replaceAll(fileNameEnv, file.getName().replaceAll(patternIn, patternOut))
                    .replaceAll(fileNameNoExtEnv, file.getName().replaceFirst("[.][^.]+$", "").replaceAll(patternIn, patternOut))
                    .replaceAll(fileInPathEnv, file.getParent().replaceAll(patternIn, patternOut))
                    .replaceAll(fileOutPathEnv, file.getParent().replaceAll(patternIn, patternOut).
                            replaceAll(
                                    inputPath.replaceAll(patternIn, patternOut + patternOut),
                                    outputPath.replaceAll(patternIn, patternOut + patternOut)
                            )
                    );
        }
        return text;
    }

    /**
     * TODO: Description of {@code setConfigValues}.
     *
     * @param jsonConfig
     * @param configMap
     */
    public static void setConfigValues(JSONObject jsonConfig, @NonNull Map<String, Config> configMap) {
        if (jsonConfig != null) {
            String key;
            for (Iterator iterator = jsonConfig.keySet().iterator(); iterator.hasNext();) {
                key = (String) iterator.next();
                Config conf = configMap.get(key);
                try {
                    if (conf != null)
                        conf.setValue(conf.getType().cast(jsonConfig.get(key)));
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("Error setting config value to '" + key + "', using default.");
                }
            }
        }
        for (Map.Entry<String, Config> entry : configMap.entrySet())
            entry.getValue().setName(entry.getKey());
    }

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
                    Thread.sleep(action.getDelay());
                } catch (InterruptedException e) {
                    System.out.println("Error executing sleep");
                }
            }
        }

        try {
            Thread.sleep(page.getDelay());
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
        String actionsPackage = "com.project.dev.selenium.generic.struct.action.";
        String chromeDriverPath = flagsMap.get("-chromeDriverPath");
        String navigationFilePath = flagsMap.get("-navigationFilePath");
        String dataFilePath = flagsMap.get("-dataFilePath");
        String outputPath = flagsMap.get("-outputPath");
        String inputPath = flagsMap.get("-inputPath");
        String urlsFilePath = flagsMap.get("-urlsFilePath");
        String chromeProfileDir = flagsMap.get("-chromeProfileDir");
        String chromeUserDataDir = System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data";
        chromeUserDataDir = FlagMap.validateFlagInMap(flagsMap, "-chromeUserDataDir", chromeUserDataDir, String.class);

        configMap = new HashMap<>();
        configMap.put("start-date", Config.builder().type(String.class).defaultValue(null).build());
        configMap.put("load-page-timeout", Config.builder().type(Long.class).defaultValue(10000l).build());
        configMap.put("max-load-page-tries", Config.builder().type(Long.class).defaultValue(3l).build());
        configMap.put("max-action-page-tries", Config.builder().type(Long.class).defaultValue(5l).build());
        configMap.put("delay-time-before-retry", Config.builder().type(Long.class).defaultValue(2000l).build());
        configMap.put("delay-time-before-end", Config.builder().type(Long.class).defaultValue(1000l).build());
        configMap.put("run-page-actions-for-each-file", Config.builder().type(Boolean.class).defaultValue(false).build());
        configMap.put("allowed-file-extensions", Config.builder().type(List.class).defaultValue(Arrays.asList(new Object[]{".jpg"})).build());

        String urlsFilePathEnv = "%urlsFilePath%";

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
            System.out.println("\nReading config files...");
            //System.out.println("");

            JSONObject jsonNavigation;
            JSONObject jsonConfig = null;
            JSONArray jsonPages = null;
            JSONObject jsonData = null;
            JSONParser parser = new JSONParser();
            try {
                jsonNavigation = (JSONObject) parser.parse(new FileReader(navigationFilePath));
                System.out.println("File: '" + navigationFilePath + "' success readed.");
                try {
                    jsonConfig = (JSONObject) jsonNavigation.get("config");
                    System.out.println("Config loaded");
                    jsonPages = (JSONArray) jsonNavigation.get("pages");
                    System.out.println("Pages loaded");
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("Error getting 'config' or 'pages'");
                    result = false;
                }
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Error reading the file: '" + navigationFilePath + "'");
                result = false;
            }
            try {
                jsonData = (JSONObject) parser.parse(new FileReader(dataFilePath));
                System.out.println("File: '" + dataFilePath + "' success readed.");
                System.out.println("Data loaded");
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Error reading the file: '" + dataFilePath + "'");
                result = false;
            }

            setConfigValues(jsonConfig, configMap);
            System.out.println("\nConfig:");
            for (String key : configMap.keySet())
                System.out.println(configMap.get(key));

            if (jsonData != null) {
                System.out.println("\nData:");
                for (Object key : jsonData.keySet())
                    System.out.println(key + " = " + jsonData.get(key));
            }

            String startDate = (String) configMap.get("start-date").getCanonicalValue();
            int loadPageTimeOut = (int) (long) configMap.get("load-page-timeout").getCanonicalValue();
            int maxLoadPageTries = (int) (long) configMap.get("max-load-page-tries").getCanonicalValue();
            int maxActionPageTries = (int) (long) configMap.get("max-action-page-tries").getCanonicalValue();
            int delayTimeBeforeRetry = (int) (long) configMap.get("delay-time-before-retry").getCanonicalValue();
            int delayTimeBeforeEnd = (int) (long) configMap.get("delay-time-before-end").getCanonicalValue();
            boolean runPageActionsForEachFile = (boolean) configMap.get("run-page-actions-for-each-file").getCanonicalValue();
            List allowedFileExtensions = (List) configMap.get("allowed-file-extensions").getCanonicalValue();

            ObjectMapper mapper = new ObjectMapper();
            List<Page> pages = new ArrayList<>();
            List<String> urlPages = new ArrayList<>();
            List<String> urlFileList = new ArrayList<>();
            List<Task> tasks = new ArrayList<>();
            List<File> files = new ArrayList<>();

            if (urlsFilePath != null)
                result = FileProcessor.forEachLine(urlsFilePath, ActionProcessor::addUrlToList, urlFileList);
            System.out.println("\nUrl list:");
            if (urlFileList.isEmpty())
                System.out.println("Empty");
            else
                for (String url : urlFileList)
                    System.out.println(url);
            System.out.println("");

            if (inputPath != null)
                FileProcessor.getFiles(new File(inputPath), (String[]) allowedFileExtensions.toArray(String[]::new), files);
            System.out.println("Files:");
            if (files.isEmpty())
                System.out.println("Empty");
            else
                files.forEach(file -> System.out.println(file));
            System.out.println("");

            if (runPageActionsForEachFile && !files.isEmpty())
                System.out.println("All actions will be executed for each file (" + files.size() + " times)");
            else {
                System.out.println("All actions will be executed one time.");
                files.clear();
                files.add(null);
            }
            if (result && jsonPages != null && jsonData != null) {
                int index = 0;
                for (Object currentPage : jsonPages) {
                    Page page = null;
                    List<Element> elementsArray = new ArrayList<>();
                    JSONArray elements = null;
                    try {
                        JSONObject jsonCurrentPage = (JSONObject) currentPage;
                        page = Page.builder()
                                .id(index++)
                                .url((String) jsonCurrentPage.get("url"))
                                .delay((long) jsonCurrentPage.get("delay-before-next"))
                                .build();
                        if (page.getUrl() == null)
                            throw new Exception("Page can't be null");
                        elements = (JSONArray) jsonCurrentPage.get("elements");
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.println("Error getting info of current page");
                        result = false;
                    }
                    if (result && page != null && elements != null) {
                        //System.out.println("Page:  " + page.getUrl());
                        //System.out.println("Delay: " + page.getDelay());
                        //System.out.println("Elements:");
                        for (Object currentElement : elements) {
                            try {
                                JSONObject jsonCurrentElement = (JSONObject) currentElement;
                                Element element = Element.builder()
                                        .id(replaceData(jsonData, (String) jsonCurrentElement.get("id")))
                                        .name(replaceData(jsonData, (String) jsonCurrentElement.get("name")))
                                        .xpath(replaceData(jsonData, (String) jsonCurrentElement.get("xpath")))
                                        .build();
                                List<Action> actions = new ArrayList<>();
                                for (Object currentAction : (JSONArray) jsonCurrentElement.get("actions")) {
                                    JSONObject jsonCurrentAction = (JSONObject) currentAction;
                                    String type = replaceData(jsonData, (String) jsonCurrentAction.get("type"));
                                    for (Iterator iterator = jsonCurrentAction.keySet().iterator(); iterator.hasNext();) {
                                        String key = (String) iterator.next();
                                        Object value = jsonCurrentAction.get(key);
                                        if (value instanceof String)
                                            jsonCurrentAction.put(key, replaceData(jsonData, (String) value));
                                        else if (value instanceof List) {
                                            List list = (List) value;
                                            for (int i = 0; i < list.size(); i++) {
                                                Object elm = list.get(i);
                                                if (elm instanceof String) {
                                                    list.remove(i);
                                                    list.add(i, replaceData(jsonData, (String) elm));
                                                }
                                            }
                                        }
                                    }
                                    String className = actionsPackage;
                                    String[] classNameAux = type.split("-");
                                    for (String name : classNameAux)
                                        className += name.substring(0, 1).toUpperCase() + name.substring(1, name.length());

                                    Class actionClass = Class.forName(className);
                                    Action action = (Action) mapper.readValue(currentAction.toString(), actionClass);

                                    if (action instanceof Navigate && ((Navigate) action).getUrl().equals(urlsFilePathEnv)) {
                                        if (!urlFileList.isEmpty()) {
                                            for (String url : urlFileList) {
                                                Navigate navigate = new Navigate();
                                                navigate.setType(action.getType());
                                                navigate.setDelay(action.getDelay());
                                                navigate.setTimeout(((Navigate) action).getTimeout());
                                                navigate.setUrl(url);
                                                actions.add(navigate);
                                            }
                                        } else {
                                            System.out.println("'" + urlsFilePathEnv + "' specified for a page, but urlFileList is empty.");
                                            result = false;
                                        }
                                    } else
                                        actions.add(action);
                                }

                                element.setActions(actions);
                                //System.out.println(element);
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
                        if (!page.getUrl().equals("%urlsFilePath%")) {
                            pages.add(page);
                            urlPages.add(page.getUrl());
                        } else if (!urlFileList.isEmpty()) {
                            index--;
                            for (String url : urlFileList) {
                                Page auxPage = Page.builder()
                                        .id(index++)
                                        .url(url)
                                        .delay(page.getDelay())
                                        .elements(elementsArray)
                                        .build();
                                pages.add(auxPage);
                                urlPages.add(url);
                            }
                        } else {
                            System.out.println("'" + urlsFilePathEnv + "' specified for a page, but urlFileList is empty.");
                            result = false;
                        }
                        //System.out.println("");
                        if (!result)
                            break;
                    }
                }
            }

            if (result) {
                Task aux = Task.builder().pages(pages).build();
                for (File file : files) {
                    Task currentTask;
                    try {
                        currentTask = (Task) aux.clone();
                        currentTask.setId(taskId++);
                        currentTask.setFile(file);
                        tasks.add(currentTask);
                    } catch (CloneNotSupportedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                for (Task task : tasks) {
                    for (Page page : task.getPages()) {
                        String url = page.getUrl();
                        page.setUrl(replaceFileEnvs(task.getFile(), page.getUrl(), inputPath, outputPath, false));
                        for (Element element : page.getElements()) {
                            element.setId(replaceFileEnvs(task.getFile(), element.getId(), inputPath, outputPath, false));
                            element.setName(replaceFileEnvs(task.getFile(), element.getName(), inputPath, outputPath, false));
                            element.setXpath(replaceFileEnvs(task.getFile(), element.getXpath(), inputPath, outputPath, false));
                            for (int i = 0; i < element.getActions().size(); i++) {
                                Action action = element.getActions().get(i);
                                String className = actionsPackage;
                                String[] classNameAux = action.getType().split("-");
                                for (String name : classNameAux)
                                    className += name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
                                try {
                                    String actionStr = mapper.writeValueAsString(action);
                                    actionStr = replaceFileEnvs(task.getFile(), actionStr, inputPath, outputPath, true);
                                    Class actionClass = Class.forName(className);
                                    Action actionAux = (Action) mapper.readValue(actionStr, actionClass);
                                    element.getActions().remove(i);
                                    element.getActions().add(i, actionAux);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    }
                }

                System.out.println("\nTasks:");
                for (Task task : tasks) {
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
                System.out.println("");

                System.out.println("start-date: " + startDate);
                Date execDate = null;
                Date currentDate = new Date();
                if (startDate != null) {
                    try {
                        execDate = DATETIME_FORMAT.parse(startDate);
                        System.out.println("Parsed using '" + DATETIME_FORMAT.toLocalizedPattern() + "'");
                    } catch (Exception e1) {
                        System.out.println("Could not parse date using '" + DATETIME_FORMAT.toLocalizedPattern() + "'");
                        try {
                            execDate = DATETIME_FORMAT.parse(DATE_FORMAT.format(currentDate) + " " + startDate);
                            System.out.println("Parsed using '" + TIME_FORMAT.toLocalizedPattern() + "'");
                            if (execDate.before(currentDate)) {
                                execDate.setTime(execDate.getTime() + 86400000);
                            }
                        } catch (Exception e2) {
                            System.out.println("Could not parse date using '" + TIME_FORMAT.toLocalizedPattern() + "'");
                        }
                    }
                }

                if (execDate != null) {
                    currentDate = new Date();
                    System.out.println("current-date: " + DATETIME_FORMAT.format(currentDate));
                    System.out.println("start-date:   " + DATETIME_FORMAT.format(execDate));
                    long milliseconds = execDate.getTime() - currentDate.getTime();
                    System.out.println("Waiting " + milliseconds / 1000 + " seconds...\n");
                    try {
                        Thread.sleep(milliseconds);
                    } catch (InterruptedException e) {
                        System.out.println("Error waiting " + milliseconds / 1000 + " seconds\n");
                    }
                } else
                    System.out.println("Start time invalid or not specified\n");

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

                for (Task task : tasks) {
                    currentIndex = 0;
                    for (String url : urlPages) {
                        if (!result)
                            break;
                        for (int i = 1; i <= maxActionPageTries; i++) {
                            if (SeleniumProcessor.forEachPage(driver, Arrays.asList(url), maxLoadPageTries,
                                    delayTimeBeforeRetry, loadPageTimeOut, ActionProcessor::runPageActions, task.getPages())) {
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
        return result;
    }

}
