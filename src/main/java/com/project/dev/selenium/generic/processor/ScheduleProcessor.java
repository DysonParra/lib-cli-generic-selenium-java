/*
 * @fileoverview    {ScheduleProcessor}
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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO: Description of {@code ScheduleProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class ScheduleProcessor {

    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * TODO: Description of {@code validateAndWait}.
     *
     * @param startDate
     */
    public static void validateAndWait(String startDate) {
        System.out.println("\nstart-date: " + startDate);
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

    }

}
