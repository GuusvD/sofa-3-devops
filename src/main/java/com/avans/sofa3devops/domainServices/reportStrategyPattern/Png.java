package com.avans.sofa3devops.domainServices.reportStrategyPattern;

import com.avans.sofa3devops.domain.Project;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class Png implements IReport {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Override
    public void printReport(Project project) {
        // Path to pdf File
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String directoryPath = "./src/main/java/com/avans/sofa3devops/domainServices/reportStrategyPattern/reports/png/";
        String filePath = directoryPath + project.getName() + "_report_" + timestamp + ".png";

        // Create directory if it doesn't exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create parent and all ancestor directories
        }

        // Create a BufferedImage
        int width = 600; // Adjust width as needed
        int height = 400; // Adjust height as needed
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Get Graphics2D object from the BufferedImage
        Graphics2D g2d = image.createGraphics();

        // Draw project information onto the image
        g2d.drawString("Project Name: " + project.getName(), 50, 50);
        // You can include more project information as needed

        // Dispose of the Graphics2D object
        g2d.dispose();

        // Write the BufferedImage to a PNG file
        try {
            // Write the BufferedImage to a PNG file
            File outputFile = new File(filePath);
            ImageIO.write(image, "png", outputFile);
            logger.info("PNG file saved successfully.");
        } catch (IOException e) {
            logger.info("Error writing PNG file: " + e.getMessage());
        }
    }
}