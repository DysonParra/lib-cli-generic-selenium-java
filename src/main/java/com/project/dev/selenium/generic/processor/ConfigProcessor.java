/*
 * @fileoverview    {ConfigProcessor}
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

import com.project.dev.selenium.generic.struct.Config;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.json.simple.JSONObject;

/**
 * TODO: Description of {@code ConfigProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class ConfigProcessor {

    /**
     * TODO: Description of {@code loadConfigFromJson}.
     *
     * @param jsonNavigation
     * @return
     */
    public static JSONObject loadConfigFromJson(JSONObject jsonNavigation) {
        JSONObject jsonConfig = null;
        try {
            jsonConfig = (JSONObject) jsonNavigation.get("config");
            System.out.println("Config loaded");
        } catch (Exception e) {
            System.out.println("Error getting 'config'");
        }
        return jsonConfig;
    }

    /**
     * TODO: Description of {@code initConfigMap}.
     *
     * @return
     */
    public static Map<String, Config> initConfigMap() {
        Map<String, Config> configMap = new HashMap<>();
        configMap.put("start-date", Config.builder().type(String.class).defaultValue(null).build());
        configMap.put("load-page-timeout", Config.builder().type(Long.class).defaultValue(10000l).build());
        configMap.put("max-load-page-tries", Config.builder().type(Long.class).defaultValue(3l).build());
        configMap.put("max-action-page-tries", Config.builder().type(Long.class).defaultValue(5l).build());
        configMap.put("delay-time-before-retry", Config.builder().type(Long.class).defaultValue(2000l).build());
        configMap.put("delay-time-before-end", Config.builder().type(Long.class).defaultValue(1000l).build());
        configMap.put("run-page-actions-for-each-file", Config.builder().type(Boolean.class).defaultValue(false).build());
        configMap.put("allowed-file-extensions", Config.builder().type(List.class).defaultValue(Arrays.asList(new Object[]{".jpg"})).build());
        return configMap;
    }

    /**
     * TODO: Description of {@code setConfigValues}.
     *
     * @param jsonConfig
     * @param configMap
     */
    public static void setConfigValues(JSONObject jsonConfig, @NonNull Map<String, Config> configMap) {
        if (jsonConfig != null) {
            String key;
            for (Iterator iterator = jsonConfig.keySet().iterator(); iterator.hasNext();) {
                key = (String) iterator.next();
                Config conf = configMap.get(key);
                try {
                    if (conf != null)
                        conf.setValue(conf.getType().cast(jsonConfig.get(key)));
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("Error setting config value to '" + key + "', using default.");
                }
            }
        }
        for (Map.Entry<String, Config> entry : configMap.entrySet())
            entry.getValue().setName(entry.getKey());
    }

}
