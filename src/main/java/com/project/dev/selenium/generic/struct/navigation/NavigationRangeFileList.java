/*
 * @fileoverview    {NavigationRangeFileList}
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
import com.project.dev.selenium.generic.processor.EnvironmentProcessor;
import com.project.dev.selenium.generic.processor.RangeProcessor;
import com.project.dev.selenium.generic.struct.NavigationRange;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO: Description of {@code NavigationRangeFileList}.
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
public class NavigationRangeFileList extends NavigationRange<String> implements Cloneable {

    @JsonProperty(value = "root-path")
    protected String rootPath;
    @JsonProperty(value = "allowed-file-extensions")
    protected List<String> allowedFileExtensions;

    /**
     * Obtiene las lista correspondiente al rango de navegación indicado.
     *
     * @return {@code List<T>} con el rango correspondiente.
     */
    @Override
    public List<String> getRange() {
        return RangeProcessor.getRangeFileList(rootPath, allowedFileExtensions);
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
        return EnvironmentProcessor.replaceFileEnvs(
                super.replaceEnvs(text, currentRangeValue),
                EnvironmentProcessor.CURRENT_NAV_RANGE_ENV,
                currentRangeValue,
                rootPath
        );
    }

}
