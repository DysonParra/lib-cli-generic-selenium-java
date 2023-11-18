/*
 * @fileoverview    {NavigationRangeFileLines}
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
package com.project.dev.selenium.generic.struct.navigation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.dev.selenium.generic.processor.RangeProcessor;
import com.project.dev.selenium.generic.struct.NavigationRange;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO: Description of {@code NavigationRangeFileLines}.
 *
 * @author Dyson Parra
 * @since 11
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NavigationRangeFileLines extends NavigationRange<String> implements Cloneable {

    @JsonProperty(value = "input-file-path")
    protected String urlListFilePath;
    @JsonProperty(value = "line-pattern")
    protected String linePattern;

    /**
     * Obtiene las lista correspondiente al rango de navegación indicado.
     *
     * @return {@code List<T>} con el rango correspondiente.
     */
    @Override
    public List<String> getRange() {
        return RangeProcessor.getRangeFileLines(urlListFilePath, linePattern);
    }

}
