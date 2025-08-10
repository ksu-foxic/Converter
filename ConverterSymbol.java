package ru.netology.graphics.image;

import java.awt.*;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class ConverterSymbol implements TextGraphicsConverter {

    private double maxRatio = 0;
    private int maxWidth = 0;
    private int maxHeight = 0;
    private TextColorSchema schema = new ChangeColor();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        if (img == null) throw new IOException("Не удалось загрузить изображение по URL" + url);

        double ratio = (double) img.getWidth() / img.getHeight();
        if (maxRatio > 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        if (maxWidth > 0 && newWidth > maxWidth) {
            newWidth = maxWidth;
            newHeight = (img.getHeight() * newHeight / img.getWidth());
        }

        if (maxHeight > 0 && newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (img.getWidth() * newHeight / img.getHeight());
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder symbol = new StringBuilder();
        for (int y = 0; y < bwImg.getHeight(); y++) {
            for (int x = 0; x < bwImg.getWidth(); x++) {
                int color = bwRaster.getSample(x, y, 0);
                char ch2 = schema.convert(color);
                symbol.append(ch2).append(ch2);
            }
            symbol.append('\n');
        }
        return symbol.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        if (maxWidth == 0) {
            this.maxWidth = width;
        }
    }

    @Override
    public void setMaxHeight(int height) {
        if (maxHeight == 0) {
            this.maxHeight = height;
        }
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        if (maxRatio == 0) {
            this.maxRatio = maxRatio;
        }
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        if (schema != null) {
            this.schema = schema;
        }
    }
}
