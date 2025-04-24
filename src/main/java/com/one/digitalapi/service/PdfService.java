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
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.one.digitalapi.config.ReservationProperties;
import com.one.digitalapi.entity.Passenger;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.exception.ReservationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    @Autowired
    private ReservationHelperService reservationHelperService;

    @Autowired
    private ReservationProperties reservationProperties;

    private static final String FONT_BOLD = "Helvetica-Bold";
    private static final String REFUND_POLICY = "• 100% refund before 24 hrs of journey.\n• 50% refund within 24 hrs.\n• No refund after departure.";


    public byte[] generateFormattedTicket(Integer reservationId) throws Exception {

        Reservations reservation = reservationHelperService.getReservationWithDetails(reservationId);

        if (reservation == null) {
            throw new ReservationException("Reservation not found for ID: " + reservationId);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);

        Document doc = new Document(pdf, PageSize.A4);
        doc.setMargins(20, 20, 20, 20);


        // Start
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/images/logo.png");
        if (is == null) {
            throw new RuntimeException("Logo image not found in classpath!");
        }
        byte[] imageBytes = is.readAllBytes();
        ImageData imageData = ImageDataFactory.create(imageBytes);


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
        doc.add(logo);
        // End --------------------------

        // Continue your existing content below
        PdfFont bold = PdfFontFactory.createFont(FONT_BOLD);
        PdfFont regular = PdfFontFactory.createFont("Helvetica");
        
        Style headerStyle = new Style().setFont(bold).setFontSize(12).setFontColor(ColorConstants.BLACK);
        Style normalStyle = new Style().setFont(regular).setFontSize(10);

        // Header Title with Larger Font
        Paragraph title = new Paragraph("Bus Ticket - Orange Motions").setFont(bold).setFontSize(22).setFontColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER).setBold();

        doc.add(title);

        // PNR and Status with Styling
        Table pnrTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                .useAllAvailableWidth();

        pnrTable.addCell(new Cell()
                .add(new Paragraph("PNR").setFont(bold).setFontSize(10))
                .add(new Paragraph(String.valueOf(reservation.getReservationId())).setFont(regular).setFontSize(12))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT));

        pnrTable.addCell(new Cell()
                .add(new Paragraph("Status").setFont(bold).setFontSize(10))
                .add(new Paragraph(reservation.getReservationStatus()).setFont(regular).setFontSize(12))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER));

        pnrTable.addCell(new Cell()
                .add(new Paragraph("Order ID").setFont(bold).setFontSize(10))
                .add(new Paragraph(String.valueOf(reservation.getOrderId())).setFont(regular).setFontSize(12))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT));


        doc.add(pnrTable);

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
        
        
        // Boarding point and time & dropping point and time
        Table pointsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth().setMarginBottom(10);

        // Boarding Point Cell
        Paragraph boardingTitle = new Paragraph("Boarding Point").setFontColor(ColorConstants.GRAY).setFontSize(10).setBold();
        Paragraph boardingMain = new Paragraph(reservation.getPickupAddress()).setFontSize(10).setBold().setMarginBottom(0);
        Cell boardingCell = new Cell().add(boardingTitle).add(boardingMain).setBorder(Border.NO_BORDER);
        pointsTable.addCell(boardingCell);

        Paragraph dropTitle = new Paragraph("Dropping Point").setFontColor(ColorConstants.GRAY).setFontSize(10).setBold();
        Paragraph dropMain = new Paragraph(reservation.getDropAddress()).setFontSize(10).setBold().setMarginBottom(0);
        Cell dropCell = new Cell().add(dropTitle).add(dropMain).setBorder(Border.NO_BORDER);
        pointsTable.addCell(dropCell);

        // Dropping Time Cell
        Paragraph boardingTimeTitle = new Paragraph("Boarding Time").setFontColor(ColorConstants.GRAY).setFontSize(10).setBold();
        Paragraph boardingTimeMain = new Paragraph(reservation.getPickupTime()).setFontSize(10).setBold().setMarginBottom(0);
        Cell boardingTimeCell = new Cell().add(boardingTimeTitle).add(boardingTimeMain).setBorder(Border.NO_BORDER);
        pointsTable.addCell(boardingTimeCell);

        Paragraph dropTimeTitle = new Paragraph("Dropping Time").setFontColor(ColorConstants.GRAY).setFontSize(10).setBold();
        Paragraph dropTimeMain = new Paragraph(reservation.getDropTime()).setFontSize(10).setBold().setMarginBottom(0);
        Cell dropTimeCell = new Cell().add(dropTimeTitle).add(dropTimeMain).setBorder(Border.NO_BORDER);
        pointsTable.addCell(dropTimeCell);

        doc.add(pointsTable);
        // End Boarding point and time & dropping point and time


        doc.add(new Paragraph("\n"));

        // Passenger Info Table
        Paragraph travellerDetail = new Paragraph("Traveller Details").setFont(regular).setFontSize(12).
                setTextAlignment(TextAlignment.LEFT).setFontColor(ColorConstants.DARK_GRAY).setBold();
        doc.add(travellerDetail);
        
        Table passengerTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 2}));
        passengerTable.setWidth(UnitValue.createPercentValue(100));
        passengerTable.addHeaderCell(createCell("Name", headerStyle));
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
        Paragraph paymentDetails = new Paragraph("Fare & Payment Details").setFont(regular).setFontSize(14).
                setTextAlignment(TextAlignment.LEFT).setFontColor(ColorConstants.BLACK).setBold();
        doc.add(paymentDetails);

        doc.add(new Paragraph("Base Fare  ₹ (" +  reservation.getNoOfSeatsBooked() + " Traveller) : "
                + reservation.getFare() + " Rs").addStyle(normalStyle));
        double subTotal = reservation.getFare();
        if (reservation.getDiscount() != null && reservation.getDiscountAmount() != null && reservation.getDiscountAmount() > 0) {
            doc.add(new Paragraph("Discount (" + reservation.getDiscount().getCode() + "): -₹ "
                    + reservation.getDiscountAmount() + " Rs").addStyle(normalStyle));
            subTotal -= reservation.getDiscountAmount(); // Subtotal after discount

        }
        doc.add(new Paragraph("Sub Total : ₹ " + String.format("%.2f", subTotal) + " Rs").addStyle(normalStyle));
        doc.add(new Paragraph("GST (" + reservationProperties.getGstPercentage() + "%) : ₹ "
                + String.format("%.2f", reservation.getGstAmount()) + " Rs").addStyle(normalStyle));
        doc.add(new Paragraph("Fare Paid : ₹ " + reservation.getTotalAmount()+ " Rs").addStyle(normalStyle));
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