package com.avans.sofa3devops.domainServices.reportStrategyPattern;

import com.avans.sofa3devops.domain.Project;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class Pdf implements IReport {
    @Override
    public void printReport(Project project) {
        // Path to pdf File
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String directoryPath = "./src/main/java/com/avans/sofa3devops/domainServices/reportStrategyPattern/reports/pdf/";
        String filePath = directoryPath + project.getName() + "_report_" + timestamp + ".pdf";

        // Create directory if it doesn't exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create parent and all ancestor directories
        }

        PDDocument document = new PDDocument();

        try {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("This is a sample report for " + project.getName());
            contentStream.endText();

            contentStream.close();

            document.save(filePath);
            System.out.println("PDF file saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing PDF file: " + e.getMessage());
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                System.out.println("Error while closing document: " + e.getMessage());
            }
        }
    }
}