/*
 * @overview        {NodeScreenshot}
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
package com.project.dev.selenium.generic.struct.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.Files;
import com.project.dev.selenium.generic.struct.Action;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TODO: Description of {@code NodeScreenshot}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeScreenshot extends Action {

    protected static int currentIndex;
    @JsonProperty(value = "include-image-number")
    protected boolean includeImageNumber;
    @JsonProperty(value = "output-path")
    protected String outputPath;
    @JsonProperty(value = "output-file-name-no-ext")
    protected String outputFileNameNoExt;

    /**
     * Ejecuta una acción en el elemento de la página actual.
     *
     * @param driver   es el driver del navegador.
     * @param element  es el {@code WebElement} que se le va a ejecutar dicha acción.
     * @param flagsMap contiene las {@code Flag} pasadas por consola.
     * @return {@code true} si se ejecuta la acción correctamente.
     * @throws Exception si ocurre algún error ejecutando la acción indicada.
     */
    @Override
    public boolean executeAction(@NonNull WebDriver driver, @NonNull WebElement element, Map<String, String> flagsMap) throws Exception {
        new File(outputPath).mkdirs();
        if (includeImageNumber)
            outputFileNameNoExt += "-" + String.format("%03d", ++currentIndex);
        if (NodeScreenshot.getFullNodeScreenshot(driver, element, outputPath, outputFileNameNoExt))
            return true;
        else
            throw new Exception("Screenshot could not be saved");
    }

    /**
     * Une todas las imágenes que tengan en su nombre lo indicado por {@code imageName} seguido de
     * '-i-j.png' donde 'i' y 'j' son números que representan la posición donde quedará la imágen
     * (el indice de una matriz) y guarda la imagen resultante como '{@code imageName}.png'.
     *
     * @param size       son las dimensiones que tendrá la unión de las imágenese.
     * @param outputPath ese la ruta donde se va a guardar la imagen resultante.
     * @param imageName  es el prefijo en el nombre de cada imágen.
     * @param rows       es la cantidad de imágenes de ancho.
     * @param columns    es la cantidad de imágenes de alto.
     */
    private static void mergeImages(Dimension size, String outputPath, String imageName, int rows, int columns) {
        try {
            BufferedImage outBuff = new BufferedImage(size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics graphic = outBuff.getGraphics();

            int startX = 0;
            int startY = 0;
            BufferedImage first = ImageIO.read(new File(outputPath + "\\" + imageName + "-" + (1) + "-" + (1) + ".png"));
            for (int i = 0; i < rows; i++) {
                if (i != rows - 1)
                    startX = i * first.getWidth();
                else
                    startX = size.getWidth() - first.getWidth();
                for (int j = 0; j < columns; j++) {
                    File imgFile = new File(outputPath + "\\" + imageName + "-" + (i + 1) + "-" + (j + 1) + ".png");
                    BufferedImage img = ImageIO.read(imgFile);
                    imgFile.delete();
                    if (j != columns - 1)
                        startY = j * img.getHeight();
                    else
                        startY = size.getHeight() - img.getHeight();
                    graphic.drawImage(img, startX, startY, null);
                }
            }
            ImageIO.write(outBuff, "PNG", new File(outputPath + "\\" + imageName + ".png"));
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Toma una captura de un nodo en el la página we actual de {@code WebDriver} y guarda la imagen
     * resultante.
     *
     * @param driver      es el driver del navegador.
     * @param node        es el nodo al que se le va a tomar una captura.
     * @param outputPath  es la ruta donde se va a guardar la imagen.
     * @param imgBaseName es el prefijo que va a tener cada imagen en su nombre.
     * @return Sise pudo ejecutar la tarea correctamente.
     */
    public static boolean getFullNodeScreenshot(@NonNull WebDriver driver, @NonNull WebElement node, String outputPath, String imgBaseName) {
        boolean result;
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            File scrFile;
            File output;
            Point location = node.getLocation();
            Dimension size = node.getSize();
            System.out.println("    Pos" + location);
            System.out.println("    Size" + size);

            int startX = location.getX();
            int startY = location.getY();
            int width = 0;
            int height = 0;
            int posX = 0;
            int posY = 0;
            int i = 0;
            int j = 0;

            for (i = 0; i <= 100; i++) {
                if (width * i >= size.getWidth())
                    break;
                else if ((width * (i + 1)) >= size.getWidth())
                    posX = (startX + (size.getWidth() - width));
                else
                    posX = (startX + (width * i));

                for (j = 0; j <= 100; j++) {
                    if (height * j >= size.getHeight())
                        break;
                    else if ((height * (j + 1)) >= size.getHeight())
                        posY = (startY + (size.getHeight() - height) + 1);
                    else
                        posY = (startY + (height * j) + 1);

                    js.executeScript("window.scrollTo(" + posX + "," + posY + ");");
                    scrFile = ((TakesScreenshot) node).getScreenshotAs(OutputType.FILE);
                    output = new File(outputPath + "\\" + imgBaseName + "-" + (i + 1) + "-" + (j + 1) + ".png");
                    Files.copy(scrFile, output);
                    if (j == 0) {
                        BufferedImage bimg = ImageIO.read(output);
                        width = bimg.getWidth();
                        height = bimg.getHeight();
                    }
                }
            }
            mergeImages(size, outputPath, imgBaseName, i, j);
            result = true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            result = false;
        }
        return result;
    }

}
