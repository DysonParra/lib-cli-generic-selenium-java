/*
 * @fileoverview    {SaveCurrentPage}
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
package com.project.dev.selenium.generic.struct.action;

import com.project.dev.selenium.generic.SeleniumProcessor;
import com.project.dev.selenium.generic.struct.Action;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TODO: Definición de {@code SaveCurrentPage}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
@AllArgsConstructor
@Builder
@Data
public class SaveCurrentPage extends Action {

    @Override
    public boolean executeAction(@NonNull WebDriver driver, @NonNull WebElement element) throws Exception {
        //System.out.println("SaveCurrentPage");
        String outputPath = (String) properties.get("-outputPath");
        if (value == null)
            SeleniumProcessor.getPageSource(driver, outputPath);
        else
            SeleniumProcessor.getPageSource(driver, new File(outputPath, value));
        return true;
    }

    @Override
    public String toString() {
        return "Action{" + "type=" + type + ", value=" + value + ", delay=" + delay + ", properties=" + properties + '}';
    }

}
