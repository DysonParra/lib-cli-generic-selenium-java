/*
 * @fileoverview    {SeleniumProcessor}
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

import com.project.dev.flag.processor.Flag;
import com.project.dev.flag.processor.FlagProcessor;

/**
 * TODO: Description of {@code SeleniumProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class SeleniumProcessor {

    /**
     * Procesa las flags pasadas por consola y ejecuta la navegación indicada en los archivos de
     * configuración.
     *
     * @param args argumentos de la linea de comandos.
     */
    public static void run(String[] args) {
        System.out.println("\n...START...");

        String requiredFlags[][] = {
            {"-navigationFilePath"},
            {"-dataFilePath"},
            {"-chromeDriverPath"}
        };

        String optionalFlags[][] = {
            {"-chromeProfileDir"},
            {"-chromeUserDataDir"},
            {"--notUseIncognito"}
        };

        String defaultArgs[] = {
            "-navigationFilePath",
            "res\\navigation.json",
            "-dataFilePath",
            "res\\data.json",
            "-chromeDriverPath",
            "res\\chromedriver.exe",
            "-chromeProfileDir",
            "Profile 1",
            "--notUseIncognito",};

        Flag[] flags;
        flags = FlagProcessor.convertArgsToFlags(args, defaultArgs, requiredFlags, optionalFlags, true);
        if (flags == null) {
            System.out.println("...ERROR IN FLAGS...");
            return;
        }

        FlagProcessor.printFlagsArray(flags, true);

        boolean result;
        result = SettingsProcessor.processFlags(flags);
        System.out.println("last result = " + result);
        System.out.println("...END...");
    }

}
