/*
 * @fileoverview    {ElementRangeNumeric}
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
import com.project.dev.selenium.generic.processor.RangeProcessor;
import com.project.dev.selenium.generic.struct.ElementRange;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO: Description of {@code ElementRangeNumeric}.
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
public class ElementRangeNumeric extends ElementRange<Integer> implements Cloneable {

    @JsonProperty(value = "start")
    protected Integer start;
    @JsonProperty(value = "end")
    protected Integer end;

    /**
     * Obtiene las lista correspondiente al rango de elementos indicado.
     *
     * @return {@code List<T>} con el rango correspondiente.
     */
    @Override
    public List<Integer> getRange() {
        return RangeProcessor.getRangeNumeric(start, end);
    }

}
