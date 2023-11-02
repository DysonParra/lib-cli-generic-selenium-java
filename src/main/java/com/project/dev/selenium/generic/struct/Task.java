/*
 * @fileoverview    {Task}
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO: Description of {@code Task}.
 *
 * @author Dyson Parra
 * @since 11
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Task implements Cloneable {

    private int id;
    private File file;
    @Builder.Default
    @ToString.Exclude
    private List<Page> pages = new ArrayList<>();

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
        Task copy = (Task) super.clone();
        if (this.pages != null) {
            List<Page> list = new ArrayList<>();
            pages.forEach(current -> {
                try {
                    list.add((Page) current.clone());
                } catch (CloneNotSupportedException ex) {
                    System.out.println("Error cloning: " + current);
                }
            });
            copy.setPages(list);
        }
        return copy;
    }

}
