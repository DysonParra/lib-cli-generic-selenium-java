/*
 * @fileoverview    {RobotEvent}
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

import java.awt.AWTException;
import java.awt.Robot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import static java.awt.event.KeyEvent.*;

/**
 * TODO: Description of {@code RobotEvent}.
 *
 * @author Dyson Parra
 * @since 11
 */
@AllArgsConstructor
@Builder
@Data
@ToString(callSuper = true)
public class RobotEvent {

    private Robot robot;

    /**
     * Constructor.
     *
     * @throws java.awt.AWTException
     */
    public RobotEvent() throws AWTException {
        robot = new Robot();
    }

    /**
     * Obtiene el {@code Key} de un {@code Character} en el teclado numérico.
     *
     * @param numberChar es el {@code Character} al que se le obtiene el {@code Key}.
     * @return el {@code Key} de el número en el teclado numérico.
     */
    private int getNumpad(char numberChar) {
        switch (numberChar) {
            case '0':
                return VK_NUMPAD0;
            case '1':
                return VK_NUMPAD1;
            case '2':
                return VK_NUMPAD2;
            case '3':
                return VK_NUMPAD3;
            case '4':
                return VK_NUMPAD4;
            case '5':
                return VK_NUMPAD5;
            case '6':
                return VK_NUMPAD6;
            case '7':
                return VK_NUMPAD7;
            case '8':
                return VK_NUMPAD8;
            case '9':
                return VK_NUMPAD9;
            default:
                return -1;
        }
    }

    /**
     * Escribe un {@code Character} usando {@code Robot} basado en su código ascii.
     *
     * @param numpadCodes es el código ascii del {@code Character} a escribir usando {@code Robot}
     */
    private void altNumpad(String numpadCodes) {
        if (numpadCodes == null || !numpadCodes.matches("^\\d+$"))
            return;

        robot.keyPress(VK_ALT);
        for (char charater : numpadCodes.toCharArray()) {
            int NUMPAD_KEY = getNumpad(charater);
            if (NUMPAD_KEY != -1) {
                robot.keyPress(NUMPAD_KEY);
                robot.keyRelease(NUMPAD_KEY);
            }
        }
        robot.keyRelease(VK_ALT);
    }

    /**
     * Escribe el texto almacenado en un {@code String} usando {@code Robot}
     *
     * @param text es el texto a escribir usando {@code Robot}
     */
    public void typeText(String text) {
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            typeChar(character);
        }
    }

    /**
     * Escribe un {@code Character} usando {@code Robot}
     *
     * @param asciiCode es el {@code Character} a escribir usando {@code Robot}
     */
    public void typeChar(char asciiCode) {
        switch (asciiCode) {
            case '☺':
                altNumpad("1");
                break;
            case '☻':
                altNumpad("2");
                break;
            case '♥':
                altNumpad("3");
                break;
            case '♦':
                altNumpad("4");
                break;
            case '♣':
                altNumpad("5");
                break;
            case '♠':
                altNumpad("6");
                break;
            case '•':
                altNumpad("7");
                break;
            case '◘':
                altNumpad("8");
                break;
            case '○':
                altNumpad("9");
                break;
            case '◙':
                altNumpad("10");
                break;
            case '♂':
                altNumpad("11");
                break;
            case '♀':
                altNumpad("12");
                break;
            case '♪':
                altNumpad("13");
                break;
            case '♫':
                altNumpad("14");
                break;
            case '☼':
                altNumpad("15");
                break;
            case '►':
                altNumpad("16");
                break;
            case '◄':
                altNumpad("17");
                break;
            case '↕':
                altNumpad("18");
                break;
            case '‼':
                altNumpad("19");
                break;
            case '¶':
                altNumpad("20");
                break;
            case '§':
                altNumpad("21");
                break;
            case '▬':
                altNumpad("22");
                break;
            case '↨':
                altNumpad("23");
                break;
            case '↑':
                altNumpad("24");
                break;
            case '↓':
                altNumpad("25");
                break;
            case '→':
                altNumpad("26");
                break;
            case '←':
                altNumpad("27");
                break;
            case '∟':
                altNumpad("28");
                break;
            case '↔':
                altNumpad("29");
                break;
            case '▲':
                altNumpad("30");
                break;
            case '▼':
                altNumpad("31");
                break;
            case ' ':
                altNumpad("32");
                break;
            case '!':
                altNumpad("33");
                break;
            case '"':
                altNumpad("34");
                break;
            case '#':
                altNumpad("35");
                break;
            case '$':
                altNumpad("36");
                break;
            case '%':
                altNumpad("37");
                break;
            case '&':
                altNumpad("38");
                break;
            case '\'':
                altNumpad("39");
                break;
            case '(':
                altNumpad("40");
                break;
            case ')':
                altNumpad("41");
                break;
            case '*':
                altNumpad("42");
                break;
            case '+':
                altNumpad("43");
                break;
            case ',':
                altNumpad("44");
                break;
            case '-':
                altNumpad("45");
                break;
            case '.':
                altNumpad("46");
                break;
            case '/':
                altNumpad("47");
                break;
            case '0':
                altNumpad("48");
                break;
            case '1':
                altNumpad("49");
                break;
            case '2':
                altNumpad("50");
                break;
            case '3':
                altNumpad("51");
                break;
            case '4':
                altNumpad("52");
                break;
            case '5':
                altNumpad("53");
                break;
            case '6':
                altNumpad("54");
                break;
            case '7':
                altNumpad("55");
                break;
            case '8':
                altNumpad("56");
                break;
            case '9':
                altNumpad("57");
                break;
            case ':':
                altNumpad("58");
                break;
            case ';':
                altNumpad("59");
                break;
            case '<':
                altNumpad("60");
                break;
            case '=':
                altNumpad("61");
                break;
            case '>':
                altNumpad("62");
                break;
            case '?':
                altNumpad("63");
                break;
            case '@':
                altNumpad("64");
                break;
            case 'A':
                altNumpad("65");
                break;
            case 'B':
                altNumpad("66");
                break;
            case 'C':
                altNumpad("67");
                break;
            case 'D':
                altNumpad("68");
                break;
            case 'E':
                altNumpad("69");
                break;
            case 'F':
                altNumpad("70");
                break;
            case 'G':
                altNumpad("71");
                break;
            case 'H':
                altNumpad("72");
                break;
            case 'I':
                altNumpad("73");
                break;
            case 'J':
                altNumpad("74");
                break;
            case 'K':
                altNumpad("75");
                break;
            case 'L':
                altNumpad("76");
                break;
            case 'M':
                altNumpad("77");
                break;
            case 'N':
                altNumpad("78");
                break;
            case 'O':
                altNumpad("79");
                break;
            case 'P':
                altNumpad("80");
                break;
            case 'Q':
                altNumpad("81");
                break;
            case 'R':
                altNumpad("82");
                break;
            case 'S':
                altNumpad("83");
                break;
            case 'T':
                altNumpad("84");
                break;
            case 'U':
                altNumpad("85");
                break;
            case 'V':
                altNumpad("86");
                break;
            case 'W':
                altNumpad("87");
                break;
            case 'X':
                altNumpad("88");
                break;
            case 'Y':
                altNumpad("89");
                break;
            case 'Z':
                altNumpad("90");
                break;
            case '[':
                altNumpad("91");
                break;
            case '\\':
                altNumpad("92");
                break;
            case ']':
                altNumpad("93");
                break;
            case '^':
                altNumpad("94");
                break;
            case '_':
                altNumpad("95");
                break;
            case '`':
                altNumpad("96");
                break;
            case 'a':
                altNumpad("97");
                break;
            case 'b':
                altNumpad("98");
                break;
            case 'c':
                altNumpad("99");
                break;
            case 'd':
                altNumpad("100");
                break;
            case 'e':
                altNumpad("101");
                break;
            case 'f':
                altNumpad("102");
                break;
            case 'g':
                altNumpad("103");
                break;
            case 'h':
                altNumpad("104");
                break;
            case 'i':
                altNumpad("105");
                break;
            case 'j':
                altNumpad("106");
                break;
            case 'k':
                altNumpad("107");
                break;
            case 'l':
                altNumpad("108");
                break;
            case 'm':
                altNumpad("109");
                break;
            case 'n':
                altNumpad("110");
                break;
            case 'o':
                altNumpad("111");
                break;
            case 'p':
                altNumpad("112");
                break;
            case 'q':
                altNumpad("113");
                break;
            case 'r':
                altNumpad("114");
                break;
            case 's':
                altNumpad("115");
                break;
            case 't':
                altNumpad("116");
                break;
            case 'u':
                altNumpad("117");
                break;
            case 'v':
                altNumpad("118");
                break;
            case 'w':
                altNumpad("119");
                break;
            case 'x':
                altNumpad("120");
                break;
            case 'y':
                altNumpad("121");
                break;
            case 'z':
                altNumpad("122");
                break;
            case '{':
                altNumpad("123");
                break;
            case '|':
                altNumpad("124");
                break;
            case '}':
                altNumpad("125");
                break;
            case '~':
                altNumpad("126");
                break;
            case '⌂':
                altNumpad("127");
                break;
            case 'Ç':
                altNumpad("128");
                break;
            case 'ü':
                altNumpad("129");
                break;
            case 'é':
                altNumpad("130");
                break;
            case 'â':
                altNumpad("131");
                break;
            case 'ä':
                altNumpad("132");
                break;
            case 'à':
                altNumpad("133");
                break;
            case 'å':
                altNumpad("134");
                break;
            case 'ç':
                altNumpad("135");
                break;
            case 'ê':
                altNumpad("136");
                break;
            case 'ë':
                altNumpad("137");
                break;
            case 'è':
                altNumpad("138");
                break;
            case 'ï':
                altNumpad("139");
                break;
            case 'î':
                altNumpad("140");
                break;
            case 'ì':
                altNumpad("141");
                break;
            case 'Ä':
                altNumpad("142");
                break;
            case 'Å':
                altNumpad("143");
                break;
            case 'É':
                altNumpad("144");
                break;
            case 'æ':
                altNumpad("145");
                break;
            case 'Æ':
                altNumpad("146");
                break;
            case 'ô':
                altNumpad("147");
                break;
            case 'ö':
                altNumpad("148");
                break;
            case 'ò':
                altNumpad("149");
                break;
            case 'û':
                altNumpad("150");
                break;
            case 'ù':
                altNumpad("151");
                break;
            case 'ÿ':
                altNumpad("152");
                break;
            case 'Ö':
                altNumpad("153");
                break;
            case 'Ü':
                altNumpad("154");
                break;
            case '¢':
                altNumpad("155");
                break;
            case '£':
                altNumpad("156");
                break;
            case '¥':
                altNumpad("157");
                break;
            case '₧':
                altNumpad("158");
                break;
            case 'ƒ':
                altNumpad("159");
                break;
            case 'á':
                altNumpad("160");
                break;
            case 'í':
                altNumpad("161");
                break;
            case 'ó':
                altNumpad("162");
                break;
            case 'ú':
                altNumpad("163");
                break;
            case 'ñ':
                altNumpad("164");
                break;
            case 'Ñ':
                altNumpad("165");
                break;
            case 'ª':
                altNumpad("166");
                break;
            case 'º':
                altNumpad("167");
                break;
            case '¿':
                altNumpad("168");
                break;
            case '⌐':
                altNumpad("169");
                break;
            case '¬':
                altNumpad("170");
                break;
            case '½':
                altNumpad("171");
                break;
            case '¼':
                altNumpad("172");
                break;
            case '¡':
                altNumpad("173");
                break;
            case '«':
                altNumpad("174");
                break;
            case '»':
                altNumpad("175");
                break;
            case '░':
                altNumpad("176");
                break;
            case '▒':
                altNumpad("177");
                break;
            case '▓':
                altNumpad("178");
                break;
            case '│':
                altNumpad("179");
                break;
            case '┤':
                altNumpad("180");
                break;
            case '╡':
                altNumpad("181");
                break;
            case '╢':
                altNumpad("182");
                break;
            case '╖':
                altNumpad("183");
                break;
            case '╕':
                altNumpad("184");
                break;
            case '╣':
                altNumpad("185");
                break;
            case '║':
                altNumpad("186");
                break;
            case '╗':
                altNumpad("187");
                break;
            case '╝':
                altNumpad("188");
                break;
            case '╜':
                altNumpad("189");
                break;
            case '╛':
                altNumpad("190");
                break;
            case '┐':
                altNumpad("191");
                break;
            case '└':
                altNumpad("192");
                break;
            case '┴':
                altNumpad("193");
                break;
            case '┬':
                altNumpad("194");
                break;
            case '├':
                altNumpad("195");
                break;
            case '─':
                altNumpad("196");
                break;
            case '┼':
                altNumpad("197");
                break;
            case '╞':
                altNumpad("198");
                break;
            case '╟':
                altNumpad("199");
                break;
            case '╚':
                altNumpad("200");
                break;
            case '╔':
                altNumpad("201");
                break;
            case '╩':
                altNumpad("202");
                break;
            case '╦':
                altNumpad("203");
                break;
            case '╠':
                altNumpad("204");
                break;
            case '═':
                altNumpad("205");
                break;
            case '╬':
                altNumpad("206");
                break;
            case '╧':
                altNumpad("207");
                break;
            case '╨':
                altNumpad("208");
                break;
            case '╤':
                altNumpad("209");
                break;
            case '╥':
                altNumpad("210");
                break;
            case '╙':
                altNumpad("211");
                break;
            case '╘':
                altNumpad("212");
                break;
            case '╒':
                altNumpad("213");
                break;
            case '╓':
                altNumpad("214");
                break;
            case '╫':
                altNumpad("215");
                break;
            case '╪':
                altNumpad("216");
                break;
            case '┘':
                altNumpad("217");
                break;
            case '┌':
                altNumpad("218");
                break;
            case '█':
                altNumpad("219");
                break;
            case '▄':
                altNumpad("220");
                break;
            case '▌':
                altNumpad("221");
                break;
            case '▐':
                altNumpad("222");
                break;
            case '▀':
                altNumpad("223");
                break;
            case 'α':
                altNumpad("224");
                break;
            case 'ß':
                altNumpad("225");
                break;
            case 'Γ':
                altNumpad("226");
                break;
            case 'π':
                altNumpad("227");
                break;
            case 'Σ':
                altNumpad("228");
                break;
            case 'σ':
                altNumpad("229");
                break;
            case 'µ':
                altNumpad("230");
                break;
            case 'τ':
                altNumpad("231");
                break;
            case 'Φ':
                altNumpad("232");
                break;
            case 'Θ':
                altNumpad("233");
                break;
            case 'Ω':
                altNumpad("234");
                break;
            case 'δ':
                altNumpad("235");
                break;
            case '∞':
                altNumpad("236");
                break;
            case 'φ':
                altNumpad("237");
                break;
            case 'ε':
                altNumpad("238");
                break;
            case '∩':
                altNumpad("239");
                break;
            case '≡':
                altNumpad("240");
                break;
            case '±':
                altNumpad("241");
                break;
            case '≥':
                altNumpad("242");
                break;
            case '≤':
                altNumpad("243");
                break;
            case '⌠':
                altNumpad("244");
                break;
            case '⌡':
                altNumpad("245");
                break;
            case '÷':
                altNumpad("246");
                break;
            case '≈':
                altNumpad("247");
                break;
            case '°':
                altNumpad("248");
                break;
            case '∙':
                altNumpad("249");
                break;
            case '·':
                altNumpad("250");
                break;
            case '√':
                altNumpad("251");
                break;
            case 'ⁿ':
                altNumpad("252");
                break;
            case '²':
                altNumpad("253");
                break;
            case '■':
                altNumpad("254");
                break;
            case ' ':
                altNumpad("255");
                break;
            default:
                break;
        }

    }

    /**
     * Obtiene el {@code Key} de un {@code String} que representa un {@code KeyEvent} de Java.
     *
     * @param javaCode es el {@code String} que representa un {@code KeyEvent}.
     * @return el {@code int} correspondiente al {@code String}.
     */
    public int getKeyCode(String javaCode) {
        switch (javaCode) {
            case "KEY_FIRST":
                return KEY_FIRST;
            case "KEY_LAST":
                return KEY_LAST;
            case "KEY_LOCATION_LEFT":
                return KEY_LOCATION_LEFT;
            case "KEY_LOCATION_NUMPAD":
                return KEY_LOCATION_NUMPAD;
            case "KEY_LOCATION_RIGHT":
                return KEY_LOCATION_RIGHT;
            case "KEY_LOCATION_STANDARD":
                return KEY_LOCATION_STANDARD;
            case "KEY_LOCATION_UNKNOWN":
                return KEY_LOCATION_UNKNOWN;
            case "VK_0":
                return VK_0;
            case "VK_1":
                return VK_1;
            case "VK_2":
                return VK_2;
            case "VK_3":
                return VK_3;
            case "VK_4":
                return VK_4;
            case "VK_5":
                return VK_5;
            case "VK_6":
                return VK_6;
            case "VK_7":
                return VK_7;
            case "VK_8":
                return VK_8;
            case "VK_9":
                return VK_9;
            case "VK_A":
                return VK_A;
            case "VK_ACCEPT":
                return VK_ACCEPT;
            case "VK_ADD":
                return VK_ADD;
            case "VK_AGAIN":
                return VK_AGAIN;
            case "VK_ALL_CANDIDATES":
                return VK_ALL_CANDIDATES;
            case "VK_ALPHANUMERIC":
                return VK_ALPHANUMERIC;
            case "VK_ALT":
                return VK_ALT;
            case "VK_ALT_GRAPH":
                return VK_ALT_GRAPH;
            case "VK_AMPERSAND":
                return VK_AMPERSAND;
            case "VK_ASTERISK":
                return VK_ASTERISK;
            case "VK_AT":
                return VK_AT;
            case "VK_B":
                return VK_B;
            case "VK_BACK_QUOTE":
                return VK_BACK_QUOTE;
            case "VK_BACK_SLASH":
                return VK_BACK_SLASH;
            case "VK_BACK_SPACE":
                return VK_BACK_SPACE;
            case "VK_BEGIN":
                return VK_BEGIN;
            case "VK_BRACELEFT":
                return VK_BRACELEFT;
            case "VK_BRACERIGHT":
                return VK_BRACERIGHT;
            case "VK_C":
                return VK_C;
            case "VK_CANCEL":
                return VK_CANCEL;
            case "VK_CAPS_LOCK":
                return VK_CAPS_LOCK;
            case "VK_CIRCUMFLEX":
                return VK_CIRCUMFLEX;
            case "VK_CLEAR":
                return VK_CLEAR;
            case "VK_CLOSE_BRACKET":
                return VK_CLOSE_BRACKET;
            case "VK_CODE_INPUT":
                return VK_CODE_INPUT;
            case "VK_COLON":
                return VK_COLON;
            case "VK_COMMA":
                return VK_COMMA;
            case "VK_COMPOSE":
                return VK_COMPOSE;
            case "VK_CONTEXT_MENU":
                return VK_CONTEXT_MENU;
            case "VK_CONTROL":
                return VK_CONTROL;
            case "VK_CONVERT":
                return VK_CONVERT;
            case "VK_COPY":
                return VK_COPY;
            case "VK_CUT":
                return VK_CUT;
            case "VK_D":
                return VK_D;
            case "VK_DEAD_ABOVEDOT":
                return VK_DEAD_ABOVEDOT;
            case "VK_DEAD_ABOVERING":
                return VK_DEAD_ABOVERING;
            case "VK_DEAD_ACUTE":
                return VK_DEAD_ACUTE;
            case "VK_DEAD_BREVE":
                return VK_DEAD_BREVE;
            case "VK_DEAD_CARON":
                return VK_DEAD_CARON;
            case "VK_DEAD_CEDILLA":
                return VK_DEAD_CEDILLA;
            case "VK_DEAD_CIRCUMFLEX":
                return VK_DEAD_CIRCUMFLEX;
            case "VK_DEAD_DIAERESIS":
                return VK_DEAD_DIAERESIS;
            case "VK_DEAD_DOUBLEACUTE":
                return VK_DEAD_DOUBLEACUTE;
            case "VK_DEAD_GRAVE":
                return VK_DEAD_GRAVE;
            case "VK_DEAD_IOTA":
                return VK_DEAD_IOTA;
            case "VK_DEAD_MACRON":
                return VK_DEAD_MACRON;
            case "VK_DEAD_OGONEK":
                return VK_DEAD_OGONEK;
            case "VK_DEAD_SEMIVOICED_SOUND":
                return VK_DEAD_SEMIVOICED_SOUND;
            case "VK_DEAD_TILDE":
                return VK_DEAD_TILDE;
            case "VK_DEAD_VOICED_SOUND":
                return VK_DEAD_VOICED_SOUND;
            case "VK_DECIMAL":
                return VK_DECIMAL;
            case "VK_DELETE":
                return VK_DELETE;
            case "VK_DIVIDE":
                return VK_DIVIDE;
            case "VK_DOLLAR":
                return VK_DOLLAR;
            case "VK_DOWN":
                return VK_DOWN;
            case "VK_E":
                return VK_E;
            case "VK_END":
                return VK_END;
            case "VK_ENTER":
                return VK_ENTER;
            case "VK_EQUALS":
                return VK_EQUALS;
            case "VK_ESCAPE":
                return VK_ESCAPE;
            case "VK_EURO_SIGN":
                return VK_EURO_SIGN;
            case "VK_EXCLAMATION_MARK":
                return VK_EXCLAMATION_MARK;
            case "VK_F":
                return VK_F;
            case "VK_F1":
                return VK_F1;
            case "VK_F10":
                return VK_F10;
            case "VK_F11":
                return VK_F11;
            case "VK_F12":
                return VK_F12;
            case "VK_F13":
                return VK_F13;
            case "VK_F14":
                return VK_F14;
            case "VK_F15":
                return VK_F15;
            case "VK_F16":
                return VK_F16;
            case "VK_F17":
                return VK_F17;
            case "VK_F18":
                return VK_F18;
            case "VK_F19":
                return VK_F19;
            case "VK_F2":
                return VK_F2;
            case "VK_F20":
                return VK_F20;
            case "VK_F21":
                return VK_F21;
            case "VK_F22":
                return VK_F22;
            case "VK_F23":
                return VK_F23;
            case "VK_F24":
                return VK_F24;
            case "VK_F3":
                return VK_F3;
            case "VK_F4":
                return VK_F4;
            case "VK_F5":
                return VK_F5;
            case "VK_F6":
                return VK_F6;
            case "VK_F7":
                return VK_F7;
            case "VK_F8":
                return VK_F8;
            case "VK_F9":
                return VK_F9;
            case "VK_FINAL":
                return VK_FINAL;
            case "VK_FIND":
                return VK_FIND;
            case "VK_FULL_WIDTH":
                return VK_FULL_WIDTH;
            case "VK_G":
                return VK_G;
            case "VK_GREATER":
                return VK_GREATER;
            case "VK_H":
                return VK_H;
            case "VK_HALF_WIDTH":
                return VK_HALF_WIDTH;
            case "VK_HELP":
                return VK_HELP;
            case "VK_HIRAGANA":
                return VK_HIRAGANA;
            case "VK_HOME":
                return VK_HOME;
            case "VK_I":
                return VK_I;
            case "VK_INPUT_METHOD_ON_OFF":
                return VK_INPUT_METHOD_ON_OFF;
            case "VK_INSERT":
                return VK_INSERT;
            case "VK_INVERTED_EXCLAMATION_MARK":
                return VK_INVERTED_EXCLAMATION_MARK;
            case "VK_J":
                return VK_J;
            case "VK_JAPANESE_HIRAGANA":
                return VK_JAPANESE_HIRAGANA;
            case "VK_JAPANESE_KATAKANA":
                return VK_JAPANESE_KATAKANA;
            case "VK_JAPANESE_ROMAN":
                return VK_JAPANESE_ROMAN;
            case "VK_K":
                return VK_K;
            case "VK_KANA":
                return VK_KANA;
            case "VK_KANA_LOCK":
                return VK_KANA_LOCK;
            case "VK_KANJI":
                return VK_KANJI;
            case "VK_KATAKANA":
                return VK_KATAKANA;
            case "VK_KP_DOWN":
                return VK_KP_DOWN;
            case "VK_KP_LEFT":
                return VK_KP_LEFT;
            case "VK_KP_RIGHT":
                return VK_KP_RIGHT;
            case "VK_KP_UP":
                return VK_KP_UP;
            case "VK_L":
                return VK_L;
            case "VK_LEFT":
                return VK_LEFT;
            case "VK_LEFT_PARENTHESIS":
                return VK_LEFT_PARENTHESIS;
            case "VK_LESS":
                return VK_LESS;
            case "VK_M":
                return VK_M;
            case "VK_META":
                return VK_META;
            case "VK_MINUS":
                return VK_MINUS;
            case "VK_MODECHANGE":
                return VK_MODECHANGE;
            case "VK_MULTIPLY":
                return VK_MULTIPLY;
            case "VK_N":
                return VK_N;
            case "VK_NONCONVERT":
                return VK_NONCONVERT;
            case "VK_NUM_LOCK":
                return VK_NUM_LOCK;
            case "VK_NUMBER_SIGN":
                return VK_NUMBER_SIGN;
            case "VK_NUMPAD0":
                return VK_NUMPAD0;
            case "VK_NUMPAD1":
                return VK_NUMPAD1;
            case "VK_NUMPAD2":
                return VK_NUMPAD2;
            case "VK_NUMPAD3":
                return VK_NUMPAD3;
            case "VK_NUMPAD4":
                return VK_NUMPAD4;
            case "VK_NUMPAD5":
                return VK_NUMPAD5;
            case "VK_NUMPAD6":
                return VK_NUMPAD6;
            case "VK_NUMPAD7":
                return VK_NUMPAD7;
            case "VK_NUMPAD8":
                return VK_NUMPAD8;
            case "VK_NUMPAD9":
                return VK_NUMPAD9;
            case "VK_O":
                return VK_O;
            case "VK_OPEN_BRACKET":
                return VK_OPEN_BRACKET;
            case "VK_P":
                return VK_P;
            case "VK_PAGE_DOWN":
                return VK_PAGE_DOWN;
            case "VK_PAGE_UP":
                return VK_PAGE_UP;
            case "VK_PASTE":
                return VK_PASTE;
            case "VK_PAUSE":
                return VK_PAUSE;
            case "VK_PERIOD":
                return VK_PERIOD;
            case "VK_PLUS":
                return VK_PLUS;
            case "VK_PREVIOUS_CANDIDATE":
                return VK_PREVIOUS_CANDIDATE;
            case "VK_PRINTSCREEN":
                return VK_PRINTSCREEN;
            case "VK_PROPS":
                return VK_PROPS;
            case "VK_Q":
                return VK_Q;
            case "VK_QUOTE":
                return VK_QUOTE;
            case "VK_QUOTEDBL":
                return VK_QUOTEDBL;
            case "VK_R":
                return VK_R;
            case "VK_RIGHT":
                return VK_RIGHT;
            case "VK_RIGHT_PARENTHESIS":
                return VK_RIGHT_PARENTHESIS;
            case "VK_ROMAN_CHARACTERS":
                return VK_ROMAN_CHARACTERS;
            case "VK_S":
                return VK_S;
            case "VK_SCROLL_LOCK":
                return VK_SCROLL_LOCK;
            case "VK_SEMICOLON":
                return VK_SEMICOLON;
            case "VK_SEPARATER":
                return VK_SEPARATER;
            case "VK_SEPARATOR":
                return VK_SEPARATOR;
            case "VK_SHIFT":
                return VK_SHIFT;
            case "VK_SLASH":
                return VK_SLASH;
            case "VK_SPACE":
                return VK_SPACE;
            case "VK_STOP":
                return VK_STOP;
            case "VK_SUBTRACT":
                return VK_SUBTRACT;
            case "VK_T":
                return VK_T;
            case "VK_TAB":
                return VK_TAB;
            case "VK_U":
                return VK_U;
            case "VK_UNDEFINED":
                return VK_UNDEFINED;
            case "VK_UNDERSCORE":
                return VK_UNDERSCORE;
            case "VK_UNDO":
                return VK_UNDO;
            case "VK_UP":
                return VK_UP;
            case "VK_V":
                return VK_V;
            case "VK_W":
                return VK_W;
            case "VK_WINDOWS":
                return VK_WINDOWS;
            case "VK_X":
                return VK_X;
            case "VK_Y":
                return VK_Y;
            case "VK_Z":
                return VK_Z;
            case "KEY_PRESSED":
                return KEY_PRESSED;
            case "KEY_RELEASED":
                return KEY_RELEASED;
            case "KEY_TYPED":
                return KEY_TYPED;
            default:
                return -1;
        }
    }

    /**
     * Ejecuta un {@code KeyEvent} de Java tipo {@code KeyPress} basado en su nombre.
     *
     * @param javaCodeName es el nombre del {@code KeyEvent}.
     * @return si se pudo ejecutar el {@code KeyEvent}
     */
    public boolean keyPress(String javaCodeName) {
        if (javaCodeName == null || javaCodeName.isEmpty())
            return false;
        int keyCode = getKeyCode(javaCodeName);
        if (keyCode == -1)
            return false;
        robot.keyPress(keyCode);
        return true;
    }

    /**
     * Ejecuta un {@code KeyEvent} de Java tipo {@code KeyPress} basado en su nombre.
     *
     * @param javaCodeName es el nombre del {@code KeyEvent}.
     * @return si se pudo ejecutar el {@code KeyEvent}
     */
    public boolean keyRelease(String javaCodeName) {
        if (javaCodeName == null || javaCodeName.isEmpty())
            return false;
        int keyCode = getKeyCode(javaCodeName);
        if (keyCode == -1)
            return false;
        robot.keyRelease(keyCode);
        return true;
    }

    /**
     * Ejecuta un {@code KeyEvent} para mover el mouse.
     *
     * @param x son las nuevas coordenadas en x de el mouse.
     * @param y son las nuevas coordenadas en y de el mouse.
     */
    public void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    /**
     * Ejecuta un {@code KeyEvent} para dar clic izquierdo con el mouse.
     */
    public void mouseLeftPress() {
        robot.mousePress(BUTTON1_DOWN_MASK);
    }

    /**
     * Ejecuta un {@code KeyEvent} para soltar el clic izquierdo del mouse.
     */
    public void mouseLeftRelease() {
        robot.mouseRelease(BUTTON1_DOWN_MASK);
    }

    /**
     * Ejecuta un {@code KeyEvent} para dar clic derecho con el mouse.
     */
    public void mouseRightPress() {
        robot.mousePress(BUTTON3_DOWN_MASK);
    }

    /**
     * Ejecuta un {@code KeyEvent} para soltar el clic derecho del mouse.
     */
    public void mouseRightRelease() {
        robot.mouseRelease(BUTTON3_DOWN_MASK);
    }
    
    /**
     * Ejecuta un {@code KeyEvent} para dar clic medio con el mouse.
     */
    public void mouseMiddlePress() {
        robot.mousePress(BUTTON2_DOWN_MASK);
    }

    /**
     * Ejecuta un {@code KeyEvent} para soltar el clic medio del mouse.
     */
    public void mouseMiddleRelease() {
        robot.mouseRelease(BUTTON2_DOWN_MASK);
    }

    /**
     * Ejecuta un {@code KeyEvent} para hacer scroll con el mouse.
     * @param wheelAmt es la cantidad de scroll que se desea hacer.
     */
    public void mouseWheel(int wheelAmt) {
        robot.mouseWheel(wheelAmt);
    }
}
