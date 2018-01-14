package sw_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by Starforge on 05.11.2017.
 */
public class Image {

    private final static Logger log = LogManager.getLogger("com.adbmanager.log4j2");

    public static final String scrName = "screenshot.jpg";
    public static final String rotatedScrName = Configuration.logsPath + "//screenshot-rotated.jpg";
    public static final String resizedScrName = Configuration.logsPath + "//screenshot-resized.jpg";
    public static final String origScrName = Configuration.logsPath + "//screenshot-orig.jpg";
    private static final String scrPrevName = "screenshot.jpg.bak";

    private static int height;
    private static int width;
    private static BufferedImage image;

    public static boolean getReadFromFile() throws Exception {
        try {
            File f = new File(scrName);
            image = ImageIO.read(f);
            height = image.getHeight();
            width = image.getWidth();
            log.info("Изображение "+scrName+ " с размерами ["+width+":"+height+"] найдено!");
            return true;
        }catch(IIOException iio){
            log.error("Файл изображения не найден:", iio);
            return false;
        }
    }

    public static void delete()throws Exception {
        Files.deleteIfExists(Paths.get(scrName));
    }

    public static void createCopy() throws Exception {
        Files.copy(Paths.get(scrName), Paths.get(scrName+".bak"), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void createCopy(String newName) throws Exception {
        Files.copy(Paths.get(scrName), Paths.get(newName), StandardCopyOption.REPLACE_EXISTING);
    }

    public static String imageNameSuffix = "";

    public static void drawPointOfTap(String image, int x, int y) throws IOException {
        BufferedImage origImage = ImageIO.read(new File(image));
        BufferedImage result = new BufferedImage(origImage.getWidth(), origImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawRenderedImage(origImage, null);
        g.setColor(Color.YELLOW);
        g.drawLine(0, y, origImage.getWidth(), y);
        g.drawLine(x, 0, x, origImage.getHeight());
        g.setColor(Color.WHITE);
        g.drawLine(x, y, x, y);
        g.dispose();
        ImageIO.write(result, "jpeg", new FileOutputStream(origScrName + "_" + imageNameSuffix + "_" + System.currentTimeMillis() + ".jpg"));
    }


    public static void resizeImage(int img_width, int img_height) throws IOException {
        BufferedImage resizedImage = new BufferedImage(img_width, img_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, img_width, img_height, null);
        g.dispose();
        ImageIO.write(resizedImage, "jpeg", new FileOutputStream(scrName));
    }

    public static void rotate(double angle) throws IOException {
        int w = image.getWidth(), h = image.getHeight();
        BufferedImage result = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.translate((720 - w) / 2, (1280 - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        image = result;
        ImageIO.write(result, "jpeg", new FileOutputStream(scrName));
    }

    public static void createCopy2() throws Exception {
        Files.copy(Paths.get(scrName), Paths.get(scrPrevName));
    }

    public static String getScrName() {
        return scrName;
    }

    public static String getPrevScrName() {
        return scrPrevName;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static BufferedImage getImage() {
        return image;
    }
}
