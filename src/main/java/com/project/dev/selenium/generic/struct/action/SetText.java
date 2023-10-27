/*
 * @fileoverview    {SetText}
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

import com.project.dev.selenium.generic.struct.Action;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TODO: Definición de {@code SetText}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
@AllArgsConstructor
@Builder
@Data
@ToString(callSuper = true)
public class SetText extends Action {

    @Override
    public boolean executeAction(@NonNull WebDriver driver, @NonNull WebElement element) throws Exception {
        //System.out.println("SetText");
        element.sendKeys(value);
        return true;
    }

}
