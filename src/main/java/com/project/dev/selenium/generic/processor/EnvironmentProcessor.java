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

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

/**
 * TODO: Description of {@code EnvironmentProcessor}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class EnvironmentProcessor {

    public static final String CURRENT_NAV_RANGE_ENV = "%navRangeValue%";
    public static final String CURRENT_ELM_RANGE_ENV = "%elmRangeValue%";

    public static final String CURRENT_NAV_DATE_YEAR_RANGE_ENV = "%navRangeYear%";
    public static final String CURRENT_NAV_DATE_MONTH_RANGE_ENV = "%navRangeMonth%";
    public static final String CURRENT_NAV_DATE_DAY_RANGE_ENV = "%navRangeDay%";

    public static final String CURRENT_ELM_DATE_YEAR_RANGE_ENV = "%elmRangeYear%";
    public static final String CURRENT_ELM_DATE_MONTH_RANGE_ENV = "%elmRangeMonth%";
    public static final String CURRENT_ELM_DATE_DAY_RANGE_ENV = "%elmRangeDay%";

    public static final String CURRENT_NAV_FILE_FULL_PATH_ENV = "%navFileFullPath%";
    public static final String CURRENT_NAV_FILE_PATH_ENV = "%navFilePath%";
    public static final String CURRENT_NAV_FILE_ONLY_PATH_ENV = "%navFileOnlyPath%";
    public static final String CURRENT_NAV_FILE_NAME_ENV = "%navFileName%";
    public static final String CURRENT_NAV_FILE_NAME_NO_EXT_ENV = "%navFileNameNoExt%";
    public static final String CURRENT_NAV_FILE_ROOT_PATH_ENV = "%navFileRootPath%";
    public static final String CURRENT_NAV_FILE_PATH_NO_ROOT_PATH_ENV = "%navFilePathNoRootPath%";
    public static final String CURRENT_NAV_FILE_ONLY_PATH_NO_ROOT_PATH_ENV = "%navFileOnlyPathNoRootPath%";
    public static final String CURRENT_NAV_FILE_BASE_64_ENV = "%navFileBase64%";

    public static final String CURRENT_ELM_FILE_FULL_PATH_ENV = "%elmFileFullPath%";
    public static final String CURRENT_ELM_FILE_PATH_ENV = "%elmFilePath%";
    public static final String CURRENT_ELM_FILE_ONLY_PATH_ENV = "%elmFileOnlyPath%";
    public static final String CURRENT_ELM_FILE_NAME_ENV = "%elmFileName%";
    public static final String CURRENT_ELM_FILE_NAME_NO_EXT_ENV = "%elmFileNameNoExt%";
    public static final String CURRENT_ELM_FILE_ROOT_PATH_ENV = "%elmFileRootPath%";
    public static final String CURRENT_ELM_FILE_PATH_NO_ROOT_PATH_ENV = "%elmFilePathNoRootPath%";
    public static final String CURRENT_ELM_FILE_ONLY_PATH_NO_ROOT_PATH_ENV = "%elmFileOnlyPathNoRootPath%";
    public static final String CURRENT_ELM_FILE_BASE_64_ENV = "%elmFileBase64%";

    /**
     * TODO: Description of {@code replaceEnv}.
     *
     * @param currentEnv
     * @param input
     * @param currentValue
     * @return {@code String} con los reemplazos aplicados.
     */
    public static String replaceEnv(String input, String currentEnv, Object currentValue) {
        input = input.replaceAll(currentEnv, String.valueOf(currentValue));
        return input;
    }

    /**
     * TODO: Description of {@code replaceDateEnvs}.
     *
     * @param input
     * @param currentEnv
     * @param currentValue
     * @return {@code String} con los reemplazos aplicados.
     */
    public static String replaceDateEnvs(String input, String currentEnv, String currentValue) {
        String[] date = currentValue.split("-");
        switch (currentEnv) {
            case CURRENT_NAV_RANGE_ENV:
                input = input
                        .replaceAll(CURRENT_NAV_DATE_YEAR_RANGE_ENV, date[0])
                        .replaceAll(CURRENT_NAV_DATE_MONTH_RANGE_ENV, date[1])
                        .replaceAll(CURRENT_NAV_DATE_DAY_RANGE_ENV, date[2]);
                break;
            case CURRENT_ELM_RANGE_ENV:
                input = input
                        .replaceAll(CURRENT_ELM_DATE_YEAR_RANGE_ENV, date[0])
                        .replaceAll(CURRENT_ELM_DATE_MONTH_RANGE_ENV, date[1])
                        .replaceAll(CURRENT_ELM_DATE_DAY_RANGE_ENV, date[2]);
                break;
        }
        return input;
    }

    /**
     * TODO: Description of {@code replaceFileEnvs}.
     *
     * @param input
     * @param currentEnv
     * @param currentValue
     * @param rootPath
     * @return {@code String} con los reemplazos aplicados.
     */
    public static String replaceFileEnvs(String input, String currentEnv, String currentValue, String rootPath) {
        String patternIn = "/|\\\\";
        String patternOut = System.getProperty("file.separator");
        if (patternOut.equals("\\"))
            patternOut = patternOut.repeat(8);
        File file = new File(currentValue);
        File root = new File(rootPath);
        switch (currentEnv) {
            case CURRENT_NAV_RANGE_ENV:
                input = input.replaceAll(CURRENT_NAV_FILE_FULL_PATH_ENV, file.getAbsolutePath().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_NAV_FILE_PATH_ENV, file.getPath().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_NAV_FILE_ONLY_PATH_ENV, file.getParent().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_NAV_FILE_NAME_ENV, file.getName().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_NAV_FILE_NAME_NO_EXT_ENV, file.getName().replaceFirst("[.][^.]+$", "").replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_NAV_FILE_ROOT_PATH_ENV, file.getPath().replaceAll(patternIn.repeat(1), patternOut.repeat(1))
                                .replaceFirst(String.format("%s(%s|)", root.getPath(), patternIn.repeat(1))
                                        .replaceAll(patternIn.repeat(1), patternOut.repeat(2)), ""))
                        .replaceAll(CURRENT_NAV_FILE_ONLY_PATH_NO_ROOT_PATH_ENV, file.getParent().replaceAll(patternIn.repeat(1), patternOut.repeat(1))
                                .replaceFirst(String.format("%s(%s|)", root.getPath(), patternIn.repeat(1))
                                        .replaceAll(patternIn.repeat(1), patternOut.repeat(2)), ""));
                try {
                    input = input.replaceAll(CURRENT_NAV_FILE_BASE_64_ENV, Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case CURRENT_ELM_RANGE_ENV:
                input = input.replaceAll(CURRENT_ELM_FILE_FULL_PATH_ENV, file.getAbsolutePath().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_ELM_FILE_PATH_ENV, file.getPath().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_ELM_FILE_ONLY_PATH_ENV, file.getParent().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_ELM_FILE_NAME_ENV, file.getName().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_ELM_FILE_NAME_NO_EXT_ENV, file.getName().replaceFirst("[.][^.]+$", "").replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_ELM_FILE_ROOT_PATH_ENV, root.getPath().replaceAll(patternIn, patternOut))
                        .replaceAll(CURRENT_ELM_FILE_PATH_NO_ROOT_PATH_ENV, file.getPath().replaceAll(patternIn.repeat(1), patternOut.repeat(1))
                                .replaceFirst(String.format("%s(%s|)", root.getPath(), patternIn.repeat(1))
                                        .replaceAll(patternIn.repeat(1), patternOut.repeat(2)), ""))
                        .replaceAll(CURRENT_ELM_FILE_ONLY_PATH_NO_ROOT_PATH_ENV, file.getParent().replaceAll(patternIn.repeat(1), patternOut.repeat(1))
                                .replaceFirst(String.format("%s(%s|)", root.getPath(), patternIn.repeat(1))
                                        .replaceAll(patternIn.repeat(1), patternOut.repeat(2)), ""));
                try {
                    input = input.replaceAll(CURRENT_ELM_FILE_BASE_64_ENV, Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
        }
        return input;
    }

}
