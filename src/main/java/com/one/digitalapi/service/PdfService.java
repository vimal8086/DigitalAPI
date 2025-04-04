package com.one.digitalapi.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
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

    public byte[] generateFormattedTicket(Integer reservationId) throws Exception {

        Reservations reservation = reservationService.getReservationById(reservationId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);

        Document doc = new Document(pdf, PageSize.A4);
        doc.setMargins(20, 20, 20, 20);

        // Start -----
        // ADD THIS BLOCK HERE - add your logo as background
        ImageData imageData = ImageDataFactory.create("src/main/resources/static/images/logo.png"); // or wherever your logo is
        Image logo = new Image(imageData);
        logo.scaleToFit(400, 500);     // large size
        logo.setOpacity(0.5f);         // for watermark effect

        // Center position
        float centerX = (PageSize.A4.getWidth() - logo.getImageScaledWidth()) / 2;
        float centerY = (PageSize.A4.getHeight() - logo.getImageScaledHeight()) / 2;

        logo.setFixedPosition(centerX, centerY);
        doc.add(logo);

        // End --------------------------



        // Continue your existing content below
        PdfFont bold = PdfFontFactory.createFont("Helvetica-Bold");
        PdfFont regular = PdfFontFactory.createFont("Helvetica");


        Style titleStyle = new Style().setFont(bold).setFontSize(18).setFontColor(ColorConstants.BLUE);
        Style headerStyle = new Style().setFont(bold).setFontSize(12).setFontColor(ColorConstants.BLACK);
        Style normalStyle = new Style().setFont(regular).setFontSize(10);

        // Header
        doc.add(new Paragraph("Bus Ticket (Orange Motions)").addStyle(titleStyle).setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("PNR: " + reservation.getReservationId() + "   |   Status: " + reservation.getReservationStatus()).addStyle(normalStyle));

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
        doc.add(new Paragraph("• 100% refund before 24 hrs of journey.\n• 50% refund within 24 hrs.\n• No refund after departure.").addStyle(normalStyle));

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