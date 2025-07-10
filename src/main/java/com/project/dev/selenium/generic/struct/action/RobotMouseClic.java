/*
 * @overview        {RobotMouseClic}
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.dev.selenium.generic.struct.Action;
import com.project.dev.selenium.generic.struct.RobotEvent;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TODO: Description of {@code RobotMouseClic}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RobotMouseClic extends Action {

    @JsonProperty(value = "button")
    protected String button;
    @JsonProperty(value = "event-type")
    protected String eventType;

    /**
     * Ejecuta una acción en el elemento de la página actual.
     *
     * @param driver   es el driver del navegador.
     * @param element  es el {@code WebElement} que se le va a ejecutar dicha acción.
     * @param flagsMap contiene las {@code Flag} pasadas por consola.
     * @return {@code true} si se ejecuta la acción correctamente.
     * @throws Exception si ocurre algún error ejecutando la acción indicada.
     */
    @Override
    public boolean executeAction(@NonNull WebDriver driver, @NonNull WebElement element, Map<String, String> flagsMap) throws Exception {
        RobotEvent event = new RobotEvent();
        if (eventType != null && button != null)
            if (button.equals("left") && eventType.equals("press"))
                event.mouseLeftPress();
            else if (button.equals("left") && eventType.equals("release"))
                event.mouseLeftRelease();
            else if (button.equals("right") && eventType.equals("press"))
                event.mouseRightPress();
            else if (button.equals("right") && eventType.equals("release"))
                event.mouseRightRelease();
            else if (button.equals("middle") && eventType.equals("press"))
                event.mouseMiddlePress();
            else if (button.equals("middle") && eventType.equals("release"))
                event.mouseMiddleRelease();
        return true;
    }

}
