/*
 * @fileoverview    {ActionProcessor}
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
package com.project.dev.selenium.generic.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dev.selenium.generic.struct.Action;
import com.project.dev.selenium.generic.struct.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * TODO: Description of {@code ActionProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class ActionProcessor {

    public static final String DEFAULT_ACTIONS_PACKAGE = "com.project.dev.selenium.generic.struct.action";

    /**
     * TODO: Description of {@code parseActions}.
     *
     * @param jsonInput
     * @param configMap
     * @param mapper
     * @return
     */
    public static List<Action> parseActions(JSONArray jsonInput, Map<String, Config> configMap, ObjectMapper mapper) {
        List<Action> resultList = new ArrayList<>();
        for (Object current : jsonInput) {
            try {
                JSONObject jsonCurrent = (JSONObject) current;
                String type = (String) jsonCurrent.get("type");

                String className = DEFAULT_ACTIONS_PACKAGE + '.';
                String[] classNameAux = type.split("-");
                for (String name : classNameAux)
                    className += name.substring(0, 1).toUpperCase() + name.substring(1, name.length());

                Class currentClass = Class.forName(className);
                Action entity = (Action) mapper.readValue(current.toString(), currentClass);
                if (configMap != null)
                    ConfigProcessor.setConfigValuesToObject(configMap, entity);

                resultList.add(entity);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                resultList = null;
                break;
            }
        }
        return resultList;
    }

}
