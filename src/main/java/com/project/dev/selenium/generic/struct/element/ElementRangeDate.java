/*
 * @fileoverview    {ElementRangeDate}
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
package com.project.dev.selenium.generic.struct.element;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.dev.selenium.generic.processor.EnvironmentProcessor;
import com.project.dev.selenium.generic.processor.RangeProcessor;
import com.project.dev.selenium.generic.struct.ElementRange;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO: Description of {@code ElementRangeDate}.
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
public class ElementRangeDate extends ElementRange<String> implements Cloneable {

    @JsonProperty(value = "start")
    protected String start;
    @JsonProperty(value = "end")
    protected String end;
    @JsonProperty(value = "days-loop")
    @Builder.Default
    protected Integer daysLoop = 1;

    /**
     * Obtiene las lista correspondiente al rango de navegación indicado.
     *
     * @return {@code List<T>} con el rango correspondiente.
     */
    @Override
    public List<String> getRange() {
        return RangeProcessor.getRangeDate(start, end, daysLoop);
    }

    /**
     * Reemplaza los indicativos por el valor actual del item del rango.
     *
     * @param text
     * @param currentRangeValue
     * @return {@code String} con los reemplazos aplicados.
     */
    @Override
    public String replaceEnvs(String text, String currentRangeValue) {
        return EnvironmentProcessor.replaceDateEnvs(
                super.replaceEnvs(text, currentRangeValue),
                EnvironmentProcessor.CURRENT_ELM_RANGE_ENV,
                currentRangeValue
        );
    }

}
