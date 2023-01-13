package it.unipd.dei.hyperu.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
/*
 * Copyright 2021 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class ImageGenerator {

    public static byte[] generateImageWithText(String text) throws IOException {
        int fontSize = 160;
        Font font = new Font(Font.SERIF, Font.BOLD, fontSize);

        int imageWidth = 2048;
        int imageHeight = 2048;

        double textLineHeight = 1.5;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D canvas = (Graphics2D) image.getGraphics();


        canvas.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        canvas.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        canvas.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        canvas.setFont(font);

        float[] dist = {0.0f, 1.0f};
        LinearGradientPaint gradient = new LinearGradientPaint(new Point(0, 0), new Point(imageWidth, imageHeight), dist, randomGradient());
        canvas.setPaint(gradient);
        canvas.fillRect(0, 0, imageWidth, imageHeight);

        String[] textLines = wrapText(text, font, canvas, imageWidth * 3 / 4);
        int totalHeight = (int) (fontSize * textLineHeight) * (textLines.length - 1) + fontSize;
        int offsetHeight = (int) (imageHeight - totalHeight - textLineHeight * fontSize) / 2;
        for (int i = 0; i < textLines.length; i++) {
            String line = textLines[i];
            int lineWidth = canvas.getFontMetrics(font).stringWidth(line);
            Point textPosition = new Point((imageWidth - lineWidth) / 2, offsetHeight + (int) (fontSize * (i + 1) * textLineHeight));
            canvas.setColor(Color.BLACK);
            canvas.drawString(line, (int) textPosition.getX() + 5, (int) textPosition.getY() + 5);
            canvas.setColor(Color.WHITE);
            canvas.drawString(line, (int) textPosition.getX(), (int) textPosition.getY());
        }
        canvas.dispose();

        return toByteArray(image);
    }

    private static Color[] randomGradient() {
        Color[][] gradientArray = {{Color.decode("#f093fb"), Color.decode("#f5576c")},
                {Color.decode("#2af598"), Color.decode("#009efd")},
                {Color.decode("#00f2fe"), Color.decode("#4facfe")},
                {Color.decode("#fee140"), Color.decode("#fa709a")},
                {Color.decode("#667eea"), Color.decode("#764ba2")},
                {Color.decode("#c471f5"), Color.decode("#fa71cd")}};

        return gradientArray[(int) (Math.random() * gradientArray.length)];
    }

    private static String[] wrapText(String text, Font font, Graphics2D canvas, int wrapWidth) {

        StringBuilder line = new StringBuilder();
        java.util.List<String> textLines = new java.util.ArrayList<>();

        String[] tokens = text.split("\\s+");
        for (String token : tokens) {
            int lineWidthGuess = canvas.getFontMetrics(font).stringWidth(line + token);
            if (lineWidthGuess > wrapWidth) {
                textLines.add(line.toString().trim());
                line = new StringBuilder();
            }
            line.append(token).append(" ");
        }
        if (!line.toString().isBlank())
            textLines.add(line.toString().trim());

        String[] textLinesArray = new String[textLines.size()];
        textLines.toArray(textLinesArray);
        return textLinesArray;
    }

    private static byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", byteArray);

        return byteArray.toByteArray();
    }

}
