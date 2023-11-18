/*
 * @fileoverview    {ElementRange}
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
import com.project.dev.selenium.generic.processor.EnvironmentProcessor;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO: Description of {@code ElementRange}.
 *
 * @author Dyson Parra
 * @param <T> the type of range.
 * @since 11
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ElementRange<T> extends Element implements Cloneable, Range<T> {

    @ToString.Exclude
    @JsonProperty(value = "elements")
    protected List<Element> elements;

    /**
     * Clone the current {@code Object}.
     *
     * @return a copy of the current {@code Object}
     * @throws CloneNotSupportedException if some issue cloning the current {@code Object}
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        ElementRange copy = (ElementRange) super.clone();
        if (this.elements != null) {
            List<Element> list = new ArrayList<>();
            elements.forEach(current -> {
                try {
                    list.add((Element) current.clone());
                } catch (CloneNotSupportedException ex) {
                    System.out.println("Error cloning: " + current);
                }
            });
            copy.setElements(list);
        }
        return copy;
    }

    /**
     * Reemplaza los indicativos por el valor actual del item del rango.
     *
     * @param text
     * @param currentRangeValue
     * @return {@code String} con los reemplazos aplicados.
     */
    @Override
    public String replaceEnvs(String text, T currentRangeValue) {
        return EnvironmentProcessor.replaceEnv(
                text, EnvironmentProcessor.CURRENT_ELM_RANGE_ENV, currentRangeValue
        );
    }

}
