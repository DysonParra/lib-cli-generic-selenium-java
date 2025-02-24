/*
 * @fileoverview    {Range}
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

import java.util.List;

/**
 * TODO: Description of {@code Range}.
 *
 * @author Dyson Parra
 * @param <T> the type of range.
 * @since Java 17 (LTS), Gradle 7.3
 */
public interface Range<T> extends Cloneable {

    /**
     * Reemplaza los indicativos por el valor actual del item del rango.
     *
     * @param text
     * @param currentRangeValue
     * @return {@code String} con los reemplazos aplicados.
     */
    public abstract String replaceEnvs(String text, T currentRangeValue);

    /**
     * Obtiene las lista correspondiente al rango de indicado.
     *
     * @return {@code List<T>} con el rango correspondiente.
     */
    public abstract List<T> getRange();

}
