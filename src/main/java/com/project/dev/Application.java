/*
 * @fileoverview {Application} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {Application} fue realizada el 31/07/2022.
 * @Dev - La primera version de {Application} fue escrita por Dyson A. Parra T.
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
        String argsAux[] = {
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
            "--notUseIncognito",
        };

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

        //for (String arg : args)
        //    System.out.println(arg);
        Flag[] flags = FlagProcessor.validateFlags(argsAux, requiredFlags, optionalFlags, false);
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
