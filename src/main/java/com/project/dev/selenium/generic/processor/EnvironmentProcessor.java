/*
 * @fileoverview    {EnvironmentProcessor}
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
import com.project.dev.selenium.generic.struct.Element;
import com.project.dev.selenium.generic.struct.Page;
import com.project.dev.selenium.generic.struct.Task;
import com.project.dev.selenium.generic.struct.action.Navigate;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import lombok.NonNull;

/**
 * TODO: Description of {@code EnvironmentProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class EnvironmentProcessor {

    public static String fileFullPathEnv = "%fileFullPath%";
    public static String filePathEnv = "%filePath%";
    public static String fileNameEnv = "%fileName%";
    public static String fileNameNoExtEnv = "%fileNameNoExt%";
    public static String fileInPathEnv = "%fileInPath%";
    public static String fileOutPathEnv = "%fileOutPath%";
    public static String fileBase64 = "%fileBase64%";
    public static String urlsFilePathEnv = "%urlsFilePath%";

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
            try {
                text = text.replaceAll(fileBase64, Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return text;
    }

    /**
     * TODO: Description of {@code replaceEnvsOnTasks}.
     *
     * @param taskList
     * @param actionsPackage
     * @param inputPath
     * @param outputPath
     */
    public static void replaceEnvsOnTasks(@NonNull List<Task> taskList, String actionsPackage, String inputPath, String outputPath) {
        ObjectMapper mapper = new ObjectMapper();
        for (Task task : taskList) {
            for (Page page : task.getPages()) {
                page.setUrl(replaceFileEnvs(task.getFile(), page.getUrl(), inputPath, outputPath, false));
                for (Element element : page.getElements()) {
                    element.setId(replaceFileEnvs(task.getFile(), element.getId(), inputPath, outputPath, false));
                    element.setName(replaceFileEnvs(task.getFile(), element.getName(), inputPath, outputPath, false));
                    element.setXpath(replaceFileEnvs(task.getFile(), element.getXpath(), inputPath, outputPath, false));
                    for (int i = 0; i < element.getActions().size(); i++) {
                        Action action = element.getActions().get(i);
                        String className = actionsPackage + '.';
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
    }

    /**
     * TODO: Description of {@code replaceEnvsOnPages}.
     *
     * @param urlFileList
     * @param pageList
     * @param page
     * @return
     */
    public static boolean replaceEnvsOnPages(List<String> urlFileList, List<Page> pageList, Page page) {
        boolean result = true;
        ObjectMapper mapper = new ObjectMapper();
        if (!page.getUrl().equals(urlsFilePathEnv)) {
            pageList.add(page);
        } else if (!urlFileList.isEmpty()) {
            NavigationProcessor.PAGE_INDEX--;
            List<Element> elements = page.getElements();
            page.setElements(null);
            for (String url : urlFileList) {
                try {
                    Page auxPage = mapper.readValue(mapper.writeValueAsString(page), Page.class);
                    auxPage.setId(NavigationProcessor.PAGE_INDEX++);
                    auxPage.setUrl(url);
                    auxPage.setElements(elements);
                    pageList.add(auxPage);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.out.println("'" + urlsFilePathEnv + "' specified for a page, but urlFileList is empty.");
            result = false;
        }
        return result;
    }

    /**
     * TODO: Description of {@code replaceEnvsOnActionNavigate}.
     *
     * @param urlFileList
     * @param actionList
     * @param action
     * @return
     */
    public static boolean replaceEnvsOnActionNavigate(List<String> urlFileList, List<Action> actionList, Action action) {
        boolean result = true;
        if (action instanceof Navigate && ((Navigate) action).getUrl().equals(urlsFilePathEnv)) {
            if (!urlFileList.isEmpty()) {
                for (String url : urlFileList) {
                    Navigate navigate = new Navigate();
                    navigate.setType(action.getType());
                    navigate.setDelayTimeBeforeNext(action.getDelayTimeBeforeNext());
                    navigate.setTimeout(((Navigate) action).getTimeout());
                    navigate.setUrl(url);
                    actionList.add(navigate);
                }
            } else {
                System.out.println("'" + urlsFilePathEnv + "' specified for a page, but urlFileList is empty.");
                result = false;
            }
        } else
            actionList.add(action);
        return result;
    }

}
