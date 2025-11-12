package utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VisualUtil {

    public static BufferedImage capture(WebDriver driver) {
        Screenshot screenshot = new AShot().takeScreenshot(driver);
        return screenshot.getImage();
    }

    public static boolean hasVisualChange(BufferedImage before, BufferedImage after) {
        ImageDiff diff = new ImageDiffer().makeDiff(before, after);
        return diff.hasDiff();
    }
    public static void saveImage(BufferedImage image, String name) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File output = new File("target/screenshots/" + name + "_" + timestamp + ".png");
            output.getParentFile().mkdirs(); // ensure folder exists
            ImageIO.write(image, "PNG", output);
            System.out.println("Saved screenshot: " + output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachScreenshot(BufferedImage image, String name) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
