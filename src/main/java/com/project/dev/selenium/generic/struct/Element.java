/*
 * @fileoverview    {Element}
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

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO: Description of {@code Element}.
 *
 * @author Dyson Parra
 * @since 11
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Element implements Cloneable {

    private String id;
    private String name;
    private String xpath;
    @Builder.Default
    @ToString.Exclude
    private List<Action> actions = new ArrayList<>();

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
        Element copy = (Element) super.clone();
        if (this.actions != null) {
            List<Action> list = new ArrayList<>();
            actions.forEach(current -> {
                try {
                    list.add((Action) current.clone());
                } catch (CloneNotSupportedException ex) {
                    System.out.println("Error cloning: " + current);
                }
            });
            copy.setActions(list);
        }
        return copy;
    }

}
