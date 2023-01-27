/*
 * @fileoverview    {Application} se encarga de realizar tareas específicas.
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementación realizada.
 * @version 2.0     Documentación agregada.
 */
package com.project.dev;

import com.project.dev.flag.processor.Flag;
import com.project.dev.flag.processor.FlagProcessor;
import com.project.dev.tester.SeleniumTester;

/**
 * TODO: Definición de {@code Application}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class Application {

    /**
     * Entrada principal del sistema.
     *
     * @param args argumentos de la linea de comandos.
     */
    public static void main(String[] args) {
        System.out.println("\n...START...");

        String requiredFlags[][] = {
            {"-chromeDriverPath"},
            {"-urlsFilePath"},
            {"-outputPath"}
        };

        String optionalFlags[][] = {
            {"-chromeProfileDir"},
            {"-maxLoadPageTries"},
            {"-delayTimeBeforeRetry"},
            {"-loadPageTimeOut"},
            {"-delayTimeBeforeNextPage"},
            {"--notUseIncognito"},
            {"-chromeUserDataDir"}
        };

        String defaultArgs[] = {
            "-chromeDriverPath",
            "res\\chromedriver.exe",
            "-urlsFilePath",
            "res\\urls.xml",
            "-outputPath",
            "res\\output",
            "-chromeProfileDir",
            "Profile 1",
            "-maxLoadPageTries",
            "5",
            "--notUseIncognito"
        };

        // for (String arg : args)
        //     System.out.println(arg);
        Flag[] flags;
        flags = FlagProcessor.convertArgsToFlags(args, defaultArgs, requiredFlags, optionalFlags, false);
        if (flags == null) {
            System.out.println("...ERROR IN FLAGS...");
            return;
        }

        FlagProcessor.printFlagsArray(flags, true);

        boolean result;
        result = SeleniumTester.processFlags(flags);
        System.out.println("last result = " + result);
        System.out.println("...END...");
    }

}
