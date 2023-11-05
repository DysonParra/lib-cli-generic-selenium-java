/*
 * @fileoverview    {Page}
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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO: Description of {@code Page}.
 *
 * @author Dyson Parra
 * @since 11
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Page implements Cloneable {

    private Long id;
    @JsonProperty(value = "url")
    private String url;
    @JsonProperty(value = "load-page-timeout")
    private Long loadPageTimeOut;
    @JsonProperty(value = "max-load-page-tries")
    private Long maxLoadPageTries;
    @JsonProperty(value = "max-action-page-tries")
    private Long maxActionPageTries;
    @JsonProperty(value = "delay-time-before-next")
    private Long delayTimeBeforeNext;
    @JsonProperty(value = "delay-time-before-retry")
    private Long delayTimeBeforeRetry;
    @Builder.Default
    @ToString.Exclude
    @JsonProperty(value = "elements")
    private List<Element> elements = new ArrayList<>();

    /**
     * Get the unique ID of the current {@code Object}
     *
     * @return The unique Id of the current {@code Object}
     */
    @ToString.Include
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
        Page copy = (Page) super.clone();
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

}
