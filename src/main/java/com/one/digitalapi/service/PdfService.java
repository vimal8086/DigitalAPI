package com.one.digitalapi.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.one.digitalapi.entity.Passenger;
import com.one.digitalapi.entity.Reservations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    @Autowired
    private ReservationService reservationService;

    private static final String LOGO_PATH = "src/main/resources/static/images/logo.png";
    private static final String FONT_BOLD = "Helvetica-Bold";
    private static final String REFUND_POLICY = "• 100% refund before 24 hrs of journey.\n• 50% refund within 24 hrs.\n• No refund after departure.";

    public byte[] generateFormattedTicket(Integer reservationId) throws Exception {

        Reservations reservation = reservationService.getReservationById(reservationId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);

        Document doc = new Document(pdf, PageSize.A4);
        doc.setMargins(20, 20, 20, 20);


        // Start
        ImageData imageData = ImageDataFactory.create(LOGO_PATH);
        Image logo = new Image(imageData);

        // Scale it appropriately
        logo.scaleToFit(300, 300); // adjust as needed
        logo.setOpacity(0.20f);    // subtle watermark effect

        // Center the logo
        float centerX = (PageSize.A4.getWidth() - logo.getImageScaledWidth()) / 2;
        float centerY = (PageSize.A4.getHeight() - logo.getImageScaledHeight()) / 2;

        centerY += 100;

        // Set position and rotate for diagonal cross-like watermark
        logo.setFixedPosition(centerX, centerY);
        //logo.setRotationAngle(Math.toRadians(45)); // 45 degrees cross tilt

        doc.add(logo);
        // End --------------------------

        // Continue your existing content below
        PdfFont bold = PdfFontFactory.createFont(FONT_BOLD);
        PdfFont regular = PdfFontFactory.createFont("Helvetica");


        Style titleStyle = new Style().setFont(bold).setFontSize(18).setFontColor(ColorConstants.BLUE);
        Style headerStyle = new Style().setFont(bold).setFontSize(12).setFontColor(ColorConstants.BLACK);
        Style normalStyle = new Style().setFont(regular).setFontSize(10);

        // Header Title with Larger Font
        Paragraph title = new Paragraph("Bus Ticket - Orange Motions")
                .setFont(bold)
                .setFontSize(22)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();

        doc.add(title);

        // PNR and Status with Styling
        Paragraph pnrStatus = new Paragraph("PNR: " + reservation.getReservationId() + "   |   Status: " + reservation.getReservationStatus())
                .setFont(regular)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.DARK_GRAY);

        doc.add(pnrStatus);

         // Separator Line for Better Structure
        doc.add(new LineSeparator(new SolidLine()).setMarginTop(5).setMarginBottom(10));


        // Journey Info Table
        Table journeyTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2}));
        journeyTable.setWidth(UnitValue.createPercentValue(100));

        journeyTable.addCell(createCell("From", headerStyle));
        journeyTable.addCell(createCell("To", headerStyle));
        journeyTable.addCell(createCell("Journey Date & Time", headerStyle));

        journeyTable.addCell(createCell(reservation.getSource(), normalStyle));
        journeyTable.addCell(createCell(reservation.getDestination(), normalStyle));
        journeyTable.addCell(createCell(formatDateTime(reservation.getJourneyDate()), normalStyle));

        doc.add(journeyTable);
        doc.add(new Paragraph("\n"));

        // Bus Details
        Table busTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2}));
        busTable.setWidth(UnitValue.createPercentValue(100));

        busTable.addCell(createCell("Bus Name", headerStyle));
        busTable.addCell(createCell("Bus Type", headerStyle));
        busTable.addCell(createCell("Bus Number", headerStyle));

        busTable.addCell(createCell(reservation.getBus().getBusName(), normalStyle));
        busTable.addCell(createCell(reservation.getBus().getBusType(), normalStyle));
        busTable.addCell(createCell(reservation.getBus().getBusNumber(), normalStyle));

        busTable.addCell(createCell("Driver", headerStyle));
        busTable.addCell(createCell("Contact", headerStyle));
        busTable.addCell(createCell("Departure Time", headerStyle));

        busTable.addCell(createCell(reservation.getBus().getDriverName(), normalStyle));
        busTable.addCell(createCell(reservation.getBus().getContactNumber(), normalStyle));
        busTable.addCell(createCell(reservation.getBus().getDepartureTime(), normalStyle));

        doc.add(busTable);
        doc.add(new Paragraph("\n"));

        // Pickup Point
        if (!reservation.getBus().getPickupPoints().isEmpty()) {
            var pickup = reservation.getBus().getPickupPoints().get(0);
            doc.add(new Paragraph("Boarding Point: " + pickup.getLocation() + " (" + pickup.getPickupTime() + ")")
                    .addStyle(headerStyle));
        }

        doc.add(new Paragraph("\n"));

        // Passenger Info Table
        Table passengerTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 2}));
        passengerTable.setWidth(UnitValue.createPercentValue(100));
        passengerTable.addHeaderCell(createCell("Passenger Name", headerStyle));
        passengerTable.addHeaderCell(createCell("Gender", headerStyle));
        passengerTable.addHeaderCell(createCell("Age", headerStyle));
        passengerTable.addHeaderCell(createCell("Seat No.", headerStyle));

        for (Passenger p : reservation.getPassengers()) {
            passengerTable.addCell(createCell(p.getName(), normalStyle));
            passengerTable.addCell(createCell(p.getGender(), normalStyle));
            passengerTable.addCell(createCell(String.valueOf(p.getAge()), normalStyle));
            passengerTable.addCell(createCell(p.getSeatName(), normalStyle));
        }

        doc.add(passengerTable);
        doc.add(new Paragraph("\n"));

        // Fare
        doc.add(new Paragraph("Fare Paid: ₹ " + reservation.getFare()).addStyle(headerStyle));
        doc.add(new Paragraph("\n"));

        // Cancellation
        doc.add(new Paragraph("Cancellation Policy:").addStyle(headerStyle));
        doc.add(new Paragraph(REFUND_POLICY).addStyle(normalStyle));

        doc.add(new Paragraph("\nThank you for booking with us!").addStyle(normalStyle).setTextAlignment(TextAlignment.CENTER));

        doc.close();
        return baos.toByteArray();
    }

    private Cell createCell(String text, Style style) {
        return new Cell().add(new Paragraph(text)).addStyle(style).setBorder(new SolidBorder(0.5f));
    }

    private String formatDateTime(java.time.LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"));
    }
}