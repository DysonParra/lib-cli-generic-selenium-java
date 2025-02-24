/*
 * @fileoverview    {ElementProcessor}
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
import com.project.dev.selenium.generic.struct.Element;
import com.project.dev.selenium.generic.struct.ElementRange;
import com.project.dev.selenium.generic.struct.element.DomElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * TODO: Description of {@code ElementProcessor}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class ElementProcessor {

    public static final String DEFAULT_ELEMENTS_PACKAGE = "com.project.dev.selenium.generic.struct.element";

    /**
     * TODO: Description of {@code parseElements}.
     *
     * @param jsonInput
     * @param configMap
     * @param mapper
     * @return
     */
    public static List<Element> parseElements(JSONArray jsonInput, Map<String, Config> configMap, ObjectMapper mapper) {
        List<Element> resultList = new ArrayList<>();
        String recursiveNode = "elements";
        String childNode = "actions";
        for (Object current : jsonInput) {
            try {
                JSONObject jsonCurrent = (JSONObject) current;
                JSONArray arrayRecursive = (JSONArray) jsonCurrent.get(recursiveNode);
                JSONArray arrayChild = (JSONArray) jsonCurrent.get(childNode);
                String type = (String) jsonCurrent.get("type");
                List<Element> listRecursiveAux = null;
                List<Action> listChildAux = null;

                if (arrayRecursive != null) {
                    jsonCurrent.remove(recursiveNode);
                    listRecursiveAux = ElementProcessor.parseElements(arrayRecursive, configMap, mapper);
                    if (listRecursiveAux == null) {
                        resultList = null;
                        break;
                    }
                } else if (arrayChild != null) {
                    jsonCurrent.remove(childNode);
                    listChildAux = ActionProcessor.parseActions(arrayChild, configMap, mapper);
                    if (listChildAux == null) {
                        resultList = null;
                        break;
                    }
                }

                String className = DEFAULT_ELEMENTS_PACKAGE + '.';
                String[] classNameAux = type.split("-");
                for (String name : classNameAux)
                    className += name.substring(0, 1).toUpperCase() + name.substring(1, name.length());

                Class currentClass = Class.forName(className);
                Element entity = (Element) mapper.readValue(current.toString(), currentClass);
                if (configMap != null)
                    ConfigProcessor.setConfigValuesToObject(configMap, entity);

                if (entity instanceof DomElement)
                    ((DomElement) entity).setActions(listChildAux);
                else if (entity instanceof ElementRange)
                    ((ElementRange) entity).setElements(listRecursiveAux);
                resultList.add(entity);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                resultList = null;
                break;
            }
        }
        return resultList;
    }

    /**
     * TODO: Description of {@code removeElementRanges}.
     *
     * @param elementList
     * @param currentRange
     * @param currentValue
     * @param mapper
     * @return
     *
     */
    public static List<Element> removeElementRanges(@NonNull List<Element> elementList,
            ElementRange currentRange, Object currentValue, ObjectMapper mapper) {
        List<Element> newList = new ArrayList<>();
        for (Element current : elementList) {
            if (current != null) {
                if (current instanceof ElementRange) {
                    ElementRange range = (ElementRange) current;
                    List listRange = range.getRange();
                    if (listRange == null || listRange.isEmpty()) {
                        System.out.println("\nInvalid range for " + range);
                        newList = null;
                        break;
                    } else
                        for (Object c : listRange) {
                            try {
                                List<Element> recursive = removeElementRanges(range.getElements(), range, c, mapper);
                                if (recursive == null) {
                                    newList = null;
                                    break;
                                } else
                                    newList.addAll(recursive);
                            } catch (Exception e) {
                                e.printStackTrace(System.out);
                            }
                        }
                } else if (current instanceof DomElement) {
                    try {
                        DomElement aux = (DomElement) current;
                        if (currentValue != null) {
                            String currentStr = mapper.writeValueAsString(current);
                            currentStr = currentRange.replaceEnvs(currentStr, currentValue);
                            JSONObject inputJson = (JSONObject) new JSONParser().parse(currentStr);
                            String node = "actions";
                            List<Action> actions = ActionProcessor.parseActions(
                                    (JSONArray) inputJson.get(node), null, mapper
                            );
                            inputJson.remove(node);
                            aux = mapper.readValue(inputJson.toString(), DomElement.class);
                            aux.setActions(actions);
                            if (newList != null)
                                newList.add(aux);
                        } else if (newList != null) {
                            newList.add((Element) aux.clone());
                        }
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                }
            }
        }
        return newList;
    }

}
