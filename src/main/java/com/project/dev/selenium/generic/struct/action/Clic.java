/*
 * @overview        {Clic}
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
import com.project.dev.selenium.generic.struct.Action;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TODO: Description of {@code Clic}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
@AllArgsConstructor
@Builder
@Data
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Clic extends Action {

    /**
     * Ejecuta una acción en el elemento de la página actual.
     *
     * @param driver        es el driver del navegador.
     * @param element       es el {@code WebElement} que se le va a ejecutar dicha acción.
     * @param locator       es el {@code By} que se utilizó para encontrar el elemento en el DOM.
     * @param flagsMap      contiene las {@code Flag} pasadas por consola.
     * @param scriptResults contiene los {@code Object} obtenidos de cada script ejecutado.
     * @return {@code true} si se ejecuta la acción correctamente.
     * @throws Exception si ocurre algún error ejecutando la acción indicada.
     */
    @Override
    public boolean executeAction(@NonNull WebDriver driver, @NonNull WebElement element, @NonNull By locator, Map<String, String> flagsMap, List<Object> scriptResults) throws Exception {
        element.click();
        return true;
    }

}
