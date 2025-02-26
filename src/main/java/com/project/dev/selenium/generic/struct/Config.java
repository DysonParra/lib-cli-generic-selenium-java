/*
 * @fileoverview    {Config}
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

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO: Description of {@code Page}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Config {

    private String name;
    private Class type;
    private Object defaultValue;
    private Object value;

    /**
     * Get {@code value} if is not {@code null}, or {@code defaultValue} if it is.
     *
     * @return {@code value} or {@code defaultValue}
     */
    public Object getCanonicalValue() {
        return value != null ? value : defaultValue;
    }

    /**
     * Get the current {@code Object} as {@code String}.
     *
     * @return {@code String} representing this {@code Object}.
     */
    @Override
    public String toString() {
        return "Config{" + "name=" + name
                + ", type=" + type
                + ", defaultValue=" + (defaultValue instanceof Object[] ? Arrays.toString((Object[]) defaultValue) : defaultValue)
                + ", value=" + (value instanceof Object[] ? Arrays.toString((Object[]) value) : value) + '}';
    }

}
