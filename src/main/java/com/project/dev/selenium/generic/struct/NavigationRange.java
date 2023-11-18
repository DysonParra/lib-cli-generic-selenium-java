/*
 * @fileoverview    {NavigationRange}
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
 * TODO: Description of {@code NavigationRange}.
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
public abstract class NavigationRange<T> extends Navigation implements Cloneable, Range<T> {

    @ToString.Exclude
    @JsonProperty(value = "navigation")
    protected List<Navigation> navigation;

    /**
     * Clone the current {@code Object}.
     *
     * @return a copy of the current {@code Object}
     * @throws CloneNotSupportedException if some issue cloning the current {@code Object}
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        NavigationRange copy = (NavigationRange) super.clone();
        if (this.navigation != null) {
            List<Navigation> list = new ArrayList<>();
            navigation.forEach(current -> {
                try {
                    list.add((Navigation) current.clone());
                } catch (CloneNotSupportedException ex) {
                    System.out.println("Error cloning: " + current);
                }
            });
            copy.setNavigation(list);
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
                text, EnvironmentProcessor.CURRENT_NAV_RANGE_ENV, currentRangeValue
        );
    }

}
