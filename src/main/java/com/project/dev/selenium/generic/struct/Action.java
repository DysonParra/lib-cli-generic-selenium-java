/*
 * @overview        {Action}
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
package com.project.dev.selenium.generic.struct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import lombok.ToString;

/**
 * TODO: Description of {@code Action}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Action implements Cloneable {

    @JsonProperty(value = "type")
    protected String type;
    @JsonProperty(value = "delay-time-before-next")
    protected long delayTimeBeforeNext;

    /**
     * Get the unique ID of the current {@code Object}
     *
     * @return The unique Id of the current {@code Object}
     */
    //@ToString.Include
    public int uniqueId() {
        return System.identityHashCode(this);
    }

    /**
     * Clone the current {@code Object}.
     *
     * @return a copy of the current {@code Object}
     * @throws CloneNotSupportedException if some issue cloning the current {@code Object}
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * TODO: Description of method {@code assignScriptResult}.
     *
     * @param info
     * @param scriptResults
     * @return
     */
    public static String assignScriptResult(Object info, List<Object> scriptResults) {
        String pattern = "%(lastScriptResult|scriptResult\\[(\\d+)\\])%";
        Matcher matcher = Pattern.compile(pattern).matcher(String.valueOf(info));

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String replacement = "";

            // Case 1: %lastScriptResult%
            if ("lastScriptResult".equals(matcher.group(1))) {
                if (!scriptResults.isEmpty()) {
                    replacement = String.valueOf(scriptResults.get(scriptResults.size() - 1));
                }
            } // Case 2: %scriptResult[n]%
            else if (matcher.group(2) != null) {
                int index = Integer.parseInt(matcher.group(2));
                if (index >= 0 && index < scriptResults.size()) {
                    replacement = String.valueOf(scriptResults.get(index));
                }
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }

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
    public abstract boolean executeAction(@NonNull WebDriver driver, @NonNull WebElement element, @NonNull By locator, Map<String, String> flagsMap, List<Object> scriptResults) throws Exception;

}
