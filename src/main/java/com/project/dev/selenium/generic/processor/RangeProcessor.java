/*
 * @fileoverview    {RangeProcessor}
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

import com.project.dev.file.generic.FileProcessor;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: Description of {@code RangeProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class RangeProcessor {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * TODO: Description of {@code getRangeNumeric}.
     *
     * @param start
     * @param end
     * @return
     */
    public static List<Integer> getRangeNumeric(Integer start, Integer end) {
        List<Integer> list = new ArrayList<>();
        if (start != null && end != null) {
            for (int i = start; i <= end; i++)
                list.add(i);
        }
        return list;
    }

    /**
     * TODO: Description of {@code getRangeNumeric}.
     *
     * @param start
     * @param end
     * @return
     */
    public static List<String> getRangeDate(String start, String end) {
        List<String> list = new ArrayList<>();
        if (start != null && end != null) {
            Date startDate;
            Date endDate;
            try {
                startDate = DATE_FORMAT.parse(start);
                endDate = DATE_FORMAT.parse(end);
                while (!startDate.after(endDate)) {
                    list.add(DATE_FORMAT.format(startDate));
                    startDate.setTime(startDate.getTime() + 86400000);
                }
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        return list;
    }

    /**
     * TODO: Description of {@code getRangeUrlFileList}.
     *
     * @param urlListFilePath
     * @param linePattern
     * @return
     */
    public static List<String> getRangeUrlFileList(String urlListFilePath, String linePattern) {
        List<String> list = new ArrayList<>();
        if (urlListFilePath != null && linePattern != null) {
            FileProcessor.forEachLine(urlListFilePath, (line) -> {
                if (line.matches(linePattern))
                    list.add(line);
                return true;
            });
        }
        return list;
    }

    /**
     * TODO: Description of {@code getRangeFileList}.
     *
     * @param rootPath
     * @param allowedFileExtensions
     * @return
     */
    public static List<String> getRangeFileList(String rootPath, List<String> allowedFileExtensions) {
        try {
            List<String> list = new ArrayList<>();
            List<File> files = new ArrayList<>();
            if (rootPath != null && allowedFileExtensions != null) {
                File path = new File(rootPath);
                String[] fileExtensions = new String[allowedFileExtensions.size()];
                for (int i = 0; i < fileExtensions.length; i++)
                    fileExtensions[i] = allowedFileExtensions.get(i);
                FileProcessor.getFiles(path, fileExtensions, files);
                for (File file : files)
                    list.add(file.toString().replaceAll("/|\\\\", "/"));
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }
}
