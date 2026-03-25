package com.clinica.aauca.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.File;
import java.util.Date;

public class ManualToPdfConverter {

    private static final Color PRIMARY_COLOR = new Color(13, 71, 161);
    private static final Color ACCENT_COLOR = new Color(30, 136, 229);
    
    public static void main(String[] args) {
        String baseDir = "c:/Users/hp/Documents/SGC/";
        String mdPath = baseDir + "MANUAL_TECNICO_PLATINUM_ULTRA.md";
        String pdfPath = baseDir + "MANUAL_TECNICO_PLATINUM_ULTRA.pdf";

        try (BufferedReader br = new BufferedReader(new FileReader(mdPath));
             FileOutputStream fos = new FileOutputStream(pdfPath)) {

            Document document = new Document(PageSize.A4, 50, 50, 60, 60);
            PdfWriter.getInstance(document, fos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26, PRIMARY_COLOR);
            Font h1Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, PRIMARY_COLOR);
            Font h2Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, ACCENT_COLOR);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
            Font italicFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 11, Color.GRAY);

            // Cover
            Paragraph pTitle = new Paragraph("\n\n\nMANUAL TÉCNICO INTEGRAL\n", titleFont);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(pTitle);
            
            Paragraph pSub = new Paragraph("CLÍNICA AAUCA v3.9 PLATINUM ULTRA", h2Font);
            pSub.setAlignment(Element.ALIGN_CENTER);
            document.add(pSub);
            
            document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n"));
            Paragraph pLoc = new Paragraph("Sede Central de Excelencia: Djibloho / Oyala\nGuinea Ecuatorial", normalFont);
            pLoc.setAlignment(Element.ALIGN_CENTER);
            document.add(pLoc);
            
            document.add(new Paragraph("\nFecha de Emisión Final: " + new Date().toString(), italicFont));
            document.newPage();

            String line;
            boolean inCodeBlock = false;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                // Image Handling: ![alt](path)
                if (line.startsWith("![") && line.contains("](")) {
                    String imgPathStr = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                    File imgFile = new File(baseDir + imgPathStr);
                    if (imgFile.exists()) {
                        Image img = Image.getInstance(imgFile.getAbsolutePath());
                        img.scaleToFit(450, 450);
                        img.setAlignment(Element.ALIGN_CENTER);
                        img.setSpacingBefore(15);
                        img.setSpacingAfter(15);
                        document.add(img);
                    }
                    continue;
                }

                if (line.isEmpty()) {
                    document.add(new Paragraph("\n"));
                    continue;
                }

                if (line.startsWith("```")) {
                    inCodeBlock = !inCodeBlock;
                    continue;
                }

                if (inCodeBlock) {
                    Paragraph codePara = new Paragraph(line, FontFactory.getFont(FontFactory.COURIER, 9, Color.DARK_GRAY));
                    codePara.setIndentationLeft(20);
                    document.add(codePara);
                    continue;
                }

                if (line.startsWith("# ")) {
                    Paragraph h = new Paragraph(line.substring(2).toUpperCase(), h1Font);
                    h.setSpacingBefore(20);
                    h.setSpacingAfter(10);
                    document.add(h);
                    document.add(new LineSeparator(1f, 100, PRIMARY_COLOR, Element.ALIGN_CENTER, -2));
                } else if (line.startsWith("## ")) {
                    Paragraph h = new Paragraph(line.substring(3), h2Font);
                    h.setSpacingBefore(15);
                    document.add(h);
                } else if (line.startsWith("### ")) {
                    Paragraph h = new Paragraph(line.substring(4), boldFont);
                    h.setSpacingBefore(10);
                    document.add(h);
                } else if (line.startsWith("* ")) {
                    Paragraph p = new Paragraph("• " + line.substring(2).replace("**", ""), normalFont);
                    p.setIndentationLeft(20);
                    document.add(p);
                } else {
                    String text = line.replace("**", "");
                    document.add(new Paragraph(text, normalFont));
                }
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
