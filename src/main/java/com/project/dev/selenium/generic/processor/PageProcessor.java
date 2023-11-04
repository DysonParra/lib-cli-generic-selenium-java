/*
 * @fileoverview    {PageProcessor}
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
import com.project.dev.selenium.generic.struct.Page;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * TODO: Description of {@code PageProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class PageProcessor {

    public static Long PAGE_INDEX = 0l;

    /**
     * TODO: Description of {@code loadPagesFromJson}.
     *
     * @param jsonNavigation
     * @return
     */
    public static JSONArray loadPagesFromJson(JSONObject jsonNavigation) {
        JSONArray jsonPages = null;
        try {
            jsonPages = (JSONArray) jsonNavigation.get("pages");
            System.out.println("Pages loaded");
        } catch (Exception e) {
            System.out.println("Error getting 'pages'");
        }
        return jsonPages;
    }

    /**
     * TODO: Description of {@code parsePages}.
     *
     * @param pageList
     * @param jsonPages
     * @param jsonData
     * @param urlFileList
     * @param configMap
     * @return
     */
    public static boolean parsePages(@NonNull List<Page> pageList, JSONArray jsonPages, JSONObject jsonData, List<String> urlFileList, Map<String, Config> configMap) {
        boolean result = true;
        ObjectMapper mapper = new ObjectMapper();
        for (Object currentPage : jsonPages) {
            Page page = null;
            List<Element> elementsArray = new ArrayList<>();
            JSONArray elements = null;
            try {
                JSONObject jsonCurrentPage = (JSONObject) currentPage;
                elements = (JSONArray) jsonCurrentPage.get("elements");
                jsonCurrentPage.remove("elements");
                page = mapper.readValue(jsonCurrentPage.toJSONString(), Page.class);
                page.setId(PAGE_INDEX++);
                if (page.getUrl() == null)
                    throw new Exception("Page can't be null");
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Error getting info of current page");
                result = false;
            }
            if (result && page != null && elements != null) {
                for (Object currentElement : elements) {
                    try {
                        JSONObject jsonCurrentElement = (JSONObject) currentElement;
                        Element element = Element.builder()
                                .id(DataProcessor.replaceData(jsonData, (String) jsonCurrentElement.get("id")))
                                .name(DataProcessor.replaceData(jsonData, (String) jsonCurrentElement.get("name")))
                                .xpath(DataProcessor.replaceData(jsonData, (String) jsonCurrentElement.get("xpath")))
                                .build();
                        List<Action> actionList = new ArrayList<>();
                        for (Object currentAction : (JSONArray) jsonCurrentElement.get("actions")) {
                            JSONObject jsonCurrentAction = (JSONObject) currentAction;
                            String type = DataProcessor.replaceData(jsonData, (String) jsonCurrentAction.get("type"));
                            for (Iterator iterator = jsonCurrentAction.keySet().iterator(); iterator.hasNext();) {
                                String key = (String) iterator.next();
                                Object value = jsonCurrentAction.get(key);
                                if (value instanceof String)
                                    jsonCurrentAction.put(key, DataProcessor.replaceData(jsonData, (String) value));
                                else if (value instanceof List) {
                                    List list = (List) value;
                                    for (int i = 0; i < list.size(); i++) {
                                        Object elm = list.get(i);
                                        if (elm instanceof String) {
                                            list.remove(i);
                                            list.add(i, DataProcessor.replaceData(jsonData, (String) elm));
                                        }
                                    }
                                }
                            }
                            String className = SettingsProcessor.DEFAULT_ACTIONS_PACKAGE + '.';
                            String[] classNameAux = type.split("-");
                            for (String name : classNameAux)
                                className += name.substring(0, 1).toUpperCase() + name.substring(1, name.length());

                            Class actionClass = Class.forName(className);
                            Action action = (Action) mapper.readValue(currentAction.toString(), actionClass);
                            result = EnvironmentProcessor.replaceEnvsOnActionNavigate(urlFileList, actionList, action);
                            if (!result)
                                break;
                        }
                        element.setActions(actionList);
                        elementsArray.add(element);
                    } catch (Exception e) {
                        System.out.println("Error parsing element:");
                        System.out.println(currentElement);
                        //e.printStackTrace();
                        result = false;
                        break;
                    }
                }
                page.setElements(elementsArray);
                result = EnvironmentProcessor.replaceEnvsOnPages(urlFileList, pageList, page);
                if (!result)
                    break;
            }
        }
        return result;
    }

}
