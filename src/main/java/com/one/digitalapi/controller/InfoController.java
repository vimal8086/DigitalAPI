package com.one.digitalapi.controller;

import com.one.digitalapi.entity.InfoPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/info")
@Tag(name = "Information Management", description = "APIs for managing information")
public class InfoController {

    private final Map<String, InfoPage> pageMap = new HashMap<>();

    public InfoController() {
        pageMap.put("privacy-policy", new InfoPage("privacy-policy", "Privacy Policy", getPrivacyPolicyContent()));
        pageMap.put("faqs", new InfoPage("faqs", "FAQs", getFAQContent()));
        pageMap.put("terms-orange-motion", new InfoPage("terms-orange-motion", "Terms & Conditions: Orange Motion", getTermsAndConditionsContent()));
        pageMap.put("cancellation-orange-motion", new InfoPage("cancellation-orange-motion", "Cancellation Policy: Orange Motion", getCancellationPolicyContent()));
        pageMap.put("why-travel-orange-motion", new InfoPage("why-travel-orange-motion", "Why Travel with Orange Motion?", getWhyTravelWithOrangeMotionContent()));
    }

    @GetMapping("/{key}")
    @Operation(
            summary = "Get content for specific info page",
            description = "Retrieves page content based on the provided key. Available keys: " +
                    "`privacy-policy`, `faqs`, `terms-orange-motion`, `cancellation-orange-motion`, `why-travel-orange-motion`, `terms-prashant-corner`"
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getInfoPage(@PathVariable String key) {
        InfoPage page = pageMap.get(key);
        if (page != null) {
            return ResponseEntity.ok(page);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Info page not found for key: " + key);
            return ResponseEntity.ok(response);
        }
    }

    private String getPrivacyPolicyContent() {
        return """
            <p><strong>Orange Motion Privacy Policy</strong></p>
            <p>At Orange Motion, we value your privacy and are committed to protecting your personal information. This privacy policy explains how we collect, use, and safeguard your data when you use our app to book bus tickets or access related services.</p>

            <p>By using Orange Motion, you agree to the practices outlined in this policy. If you have any questions or concerns, feel free to contact us at motionsorange@gmail.com.
             </p>

            <h4>1. Information We Collect</h4>
            <p>We collect information necessary to enhance your experience and process your bookings. This includes:
            </p>
            <ul>
                <li><strong>Personal Information:</strong> Name, Email address, Phone number, Payment details, Travel preferences</li>
                <li><strong>Non-Personal Information:</strong> Device details, IP address, Usage data</li>
            </ul>

            <h4>2. How We Use Your Information</h4>
            <p>Your information is used for the following purposes:</p>

            <ul>
                <li>Booking Services</li>
                <li>Communication</li>
                <li>Customer Support</li>
                <li>Personalization</li>
            </ul>
            <p>We do not sell or share your personal data for advertising purposes.</p>

            <h4>3. Data Security</h4>
            <p>We use industry-standard encryption and security practices to protect your data. While no system is completely secure, we regularly review and update our measures to safeguard your information.
            </p>

            <h4>4. Your Rights</h4>
            <ul>
                <li>Access Your Data</li>
                <p>Request details of the personal data we hold about you.</p>
                <li>Opt-Out</li>
                <p>Unsubscribe from non-essential communication at any time.</p>
                <li>Request Deletion</li>
                <p>Ask us to delete your personal data, subject to legal obligations.</p>
            </ul>
            <p>For assistance with any of these, contact us at motionsorange@gmail.com.</p>


            <h4>5. Data Retention</h4>
            <p>We retain your data only as long as necessary for booking services or to comply with legal requirements. Upon request, we will delete your data as per applicable regulations</p>

            <h4>6. Contact Us</h4>
            <p>Email: <a href="mailto:motionsorange@gmail.com">motionsorange@gmail.com</a></p>  \s
       \s""";
    }

    private String getFAQContent() {
        return """
        <h2>1. Ticket Booking</h2>
        <p><strong>Q1: How do I book a bus ticket?</strong><br>
        You can easily book your bus tickets through our app or website. Simply select your travel route, pick your departure time, enter passenger details, and proceed with the payment to confirm your booking.</p>

        <p><strong>Q2: What payment methods do you accept?</strong><br>
        We accept a variety of payment methods, including credit/debit cards, UPI, wallets, and net banking.</p>

        <p><strong>Q3: Can I book a ticket for someone else?</strong><br>
        Yes, you can book a ticket for another person. Just make sure to enter their details during the booking process.</p>

        <p><strong>Q4: How will I receive my ticket?</strong><br>
        Once your booking is confirmed, you will receive your ticket via email and SMS with all the necessary details.</p>

        <h2>2. Ticket Reschedule / Cancellation</h2>
        <p><strong>Q1: Can I reschedule my ticket?</strong><br>
        Yes, you can reschedule your ticket, but this is subject to availability and a rescheduling fee. You can reschedule via the app/website or by contacting customer support.</p>

        <p><strong>Q2: How do I cancel my booking?</strong><br>
        You can cancel your booking by visiting your booking details page on the app or website. Cancellation fees may apply based on the time of cancellation.</p>

        <p><strong>Q3: Will I get a refund if I cancel my ticket?</strong><br>
        Yes, a refund will be processed according to our refund policy. Please check the cancellation window and applicable fees.</p>

        <p><strong>Q4: How long does it take for the refund to process after cancellation?</strong><br>
        Refunds usually take 7–10 business days to process depending on your payment method.</p>

        <h2>3. Bus Cancellation</h2>
        <p><strong>Q1: What happens if my bus is canceled by the operator?</strong><br>
        If the bus is canceled by the operator, you will be eligible for a full refund, and we will notify you as soon as possible.</p>

        <p><strong>Q2: Will I be offered an alternative bus if my bus is canceled?</strong><br>
        Depending on the circumstances, we may offer you an alternative bus or travel options. If no alternative is available, a refund will be provided.</p>

        <h2>4. Offers</h2>
        <p><strong>Q1: Do you offer any discounts or promotions?</strong><br>
        Yes! We offer various discounts and promotional codes that are updated regularly. Keep an eye on our app or website for the latest offers.</p>

        <p><strong>Q2: How can I apply a promo code?</strong><br>
        You can apply a promo code during the payment process by entering it in the "Apply Promo Code" section.</p>

        <p><strong>Q3: Can I use multiple promo codes for a single booking?</strong><br>
        No, only one promo code can be applied per booking.</p>

        <h2>5. Payments & Refunds</h2>
        <p><strong>Q1: What should I do if my payment fails?</strong><br>
        If your payment fails, please verify your payment details and try again. If the issue persists, contact our customer support team for assistance.</p>

        <p><strong>Q2: Can I get a refund if I face payment issues?</strong><br>
        If a payment is deducted but the booking isn't confirmed, we will initiate a refund to your original payment method within 5–7 business days.</p>

        <p><strong>Q3: Will I be refunded the full amount?</strong><br>
        Refunds depend on the cancellation policy. Please refer to our Refund Policy for more details.</p>

        <h2>6. Orange Motion Wallet</h2>
        <p><strong>Q1: What is Orange Motion Wallet?</strong><br>
        The Orange Motion Wallet allows you to store credits for future bookings. You can use the credits for booking your next tickets easily.</p>

        <p><strong>Q2: How do I add money to my Orange Motion Wallet?</strong><br>
        You can add funds to your Orange Motion Wallet using a credit card, debit card, or other supported payment methods.</p>

        <p><strong>Q3: Can I withdraw money from my Orange Motion Wallet?</strong><br>
        Unfortunately, the funds in your Orange Motion Wallet are non-refundable, but you can use them for future bookings.</p>

        <h2>7. Refund Guarantee Program</h2>
        <p><strong>Q1: What is the Refund Guarantee Program?</strong><br>
        The Refund Guarantee Program ensures that if your ticket is canceled by the operator, you will receive a full refund as per the program's terms.</p>

        <p><strong>Q2: How do I enroll in the Refund Guarantee Program?</strong><br>
        You will automatically be enrolled in the Refund Guarantee Program if it is available for your booking at the time of purchase.</p>

        <h2>8. Other Queries</h2>
        <p><strong>Q1: How can I contact customer support for other queries?</strong><br>
        You can contact our customer support team through the app, website, or via email at <a href="mailto:support@orangemotion.com">support@orangemotion.com</a> for any other queries or issues you may have.</p>

        <p><strong>Q2: What is the contact number for customer support?</strong><br>
        You can reach us at +91-123-456-7890 for immediate assistance.</p>

        <h2>9. Trip Rewards</h2>
        <p><strong>Q1: What are Trip Rewards?</strong><br>
        Trip Rewards allow you to earn points for every booking made. These points can be redeemed for discounts or free services on future bookings.</p>

        <p><strong>Q2: How do I earn Trip Rewards?</strong><br>
        You earn Trip Rewards every time you complete a booking. The points will automatically be added to your account.</p>

        <p><strong>Q3: How can I redeem Trip Rewards?</strong><br>
        You can redeem your rewards at checkout while making a booking. The available reward points will be automatically applied.</p>
        """;
    }

    private String getTermsAndConditionsContent() {
        return """
        <h2>Terms & Conditions: Orange Motion</h2>
        <p>By downloading and using the Orange Motion app, you agree to the following Terms & Conditions:</p>

        <h3>1. General Terms</h3>
        <ul>
            <li>Orange Motion offers a platform for booking bus tickets through the app.</li>
            <li>You agree to use the app/website only for lawful purposes and comply with all applicable local laws.</li>
            <li>We reserve the right to modify or discontinue our services at any time without prior notice.</li>
        </ul>

        <h3>2. Booking and Payments</h3>
        <ul>
            <li>All bus ticket bookings are subject to availability.</li>
            <li>Payment for bookings is processed through third-party payment gateways integrated within the app, and you agree to pay for services booked through the app.</li>
            <li>Orange Motion is not responsible for any issues with payment processing or third-party payment providers.</li>
        </ul>

        <h3>3. Cancellations and Refunds</h3>
        <ul>
            <li>The cancellation and refund process is governed by our Cancellation Policy.</li>
            <li>Refunds, if applicable, will be issued based on the conditions specified in the cancellation policy.</li>
        </ul>

        <h3>4. User Responsibilities</h3>
        <ul>
            <li>You are responsible for providing accurate information during booking.</li>
            <li>You are responsible for checking the travel details, including date, time, and location, before confirming your booking.</li>
        </ul>

        <h3>5. Privacy and Data Protection</h3>
        <ul>
            <li>We collect and process your personal data in accordance with our <a href="/info/privacy-policy" target="_blank">Privacy Policy</a>, ensuring your privacy is protected and your data is secure.
            </li>
        </ul>

        <h3>6. Dispute Resolution</h3>
        <ul>
            <li>Any disputes arising from the use of the Orange Motion app/website will be resolved in the jurisdiction of India.</li>
            <li>Users are encouraged to contact customer support for resolution before seeking legal remedies.</li>
        </ul>

        <h3>7. Limitation of Liability</h3>
        <ul>
            <li>Orange Motion is not liable for any indirect, incidental, or consequential damages arising from the use of the app.</li>
            <li>We are not responsible for delays or cancellations caused by external factors like weather conditions or actions by third-party bus operators.</li>
        </ul>

        <h3>8. Modification of Terms</h3>
        <ul>
            <li>We may update these Terms & Conditions from time to time. Any changes will be posted in the app/website and take effect immediately after being made available to users.</li>
        </ul>
        """;
    }

    private String getCancellationPolicyContent() {
        return """
        <h2>Cancellation Policy: Orange Motion</h2>
        <p>Orange Motion aims to provide flexibility when booking bus tickets through our app. Please review our cancellation policy below.</p>

        <h3>1. Cancellation Requests</h3>
        <ul>
            <li>You can cancel your booking directly through the Orange Motion app/website at any time before your scheduled departure.</li>
            <li>Cancellations must be made at least 24 hours before the departure time to be eligible for a refund.</li>
        </ul>

        <h3>2. Refund Process</h3>
        <p>Refunds will be processed according to the following guidelines:</p>
        <ul>
            <li>More than 24 hours before departure: <strong>90% refund</strong></li>
            <li>Between 12 to 24 hours before departure: <strong>60% refund</strong></li>
            <li>Between 6 to 12 hours before departure: <strong>20% refund</strong></li>
            <li>Less than 6 hours before departure: <strong>No refund</strong></li>
        </ul>
        <p><strong>Bus Operator Cancellations or Delays:</strong> If the bus operator cancels or delays the service, a full refund will be provided, or we will offer you an alternative option.</p>
        <ul>
            <li>Cancellation charges are computed on a per-seat basis.</li>
            <li>Cancellation charges are calculated based on the service start time.</li>
            <li>Tickets cannot be canceled after the scheduled bus departure time from the first boarding point.</li>
            <li>Cancellation charges mentioned above are excluding GST.</li>
            <li>For group bookings, cancellation of individual seats is not allowed.</li>
        </ul>

        <h3>3. Non-Refundable Fees</h3>
        <ul>
            <li>Certain service fees, such as convenience or booking charges, may be non-refundable, even in the event of cancellation.</li>
        </ul>

        <h3>4. Exceptions</h3>
        <ul>
            <li>In case of emergencies or exceptional circumstances, we may provide partial refunds or reschedule your booking. Contact our support team within the app/website for further assistance.</li>
        </ul>

        <h3>5. Modifications to Bookings</h3>
        <ul>
            <li>If you wish to modify your booking, such as changing the travel date or time, please do so through the app, subject to availability. Additional charges may apply.</li>
        </ul>

        <h3>6. No-Show Policy</h3>
        <ul>
            <li>If you fail to show up for your scheduled trip without prior cancellation, you will not be eligible for any refund.</li>
        </ul>

        <p>For further inquiries or assistance regarding cancellations, please contact: <a href="mailto:motionsorange@gmail.com">motionsorange@gmail.com</a></p>
        """;
    }

    private String getWhyTravelWithOrangeMotionContent() {
        return """
        <h2>Why Travel with Orange Motion?</h2>
        <p>Traveling with Orange Motion is more than just a bus journey—it's an experience designed with your comfort, safety, and convenience in mind. From the moment you board, you’ll feel right at home. So sit back, relax, and let us take care of the rest.</p>

        <h3>Here’s What We Offer:</h3>
        <ol>
            <li><strong>Safety First:</strong> Every seat comes equipped with a seat belt to ensure your safety, no matter where your journey takes you.</li>
            <li><strong>Comfort Beyond Compare:</strong> Enjoy the luxurious 2x1 seating, giving you extra space to stretch out and relax throughout your trip.</li>
            <li><strong>Stay Charged & Connected:</strong> With USB charging ports at every seat, your devices will stay powered up. Plus, enjoy seamless Wi-Fi connectivity to keep you in touch with the world.</li>
            <li><strong>Entertainment on the Go:</strong> Choose from a variety of TV shows and music to keep you entertained, making your travel experience as enjoyable as the destination.</li>
            <li><strong>Real-Time Trip Updates:</strong> Stay informed with up-to-date trip information, so you’re never in the dark about your journey’s progress.</li>
            <li><strong>Breathe Easy:</strong> Relax in a fully air-conditioned environment, designed for your comfort no matter the weather outside.</li>
            <li><strong>Travel with Peace of Mind:</strong> We’ve got you covered with passenger insurance, so you can travel with confidence and security.</li>
            <li><strong>Clean & Hygienic:</strong> Our commitment to cleanliness means you’ll find neat, hygienic, and comfortable seats ready for you to sit back and enjoy the ride.</li>
            <li><strong>Rest Stops at Premium Hotels:</strong> We stop at top-tier hotels along the route, ensuring you get a restful break in style during your journey.</li>
            <li><strong>Stay Hydrated:</strong> We provide complimentary bottled drinking water to keep you refreshed throughout the trip.</li>
            <li><strong>We’ve Got Your Health Covered:</strong> First aid kits and necessary medical assistance are always on board, just in case you need it.</li>
            <li><strong>Fire Safety for Your Protection:</strong> Rest easy knowing that our buses are equipped with the latest fire safety measures.</li>
            <li><strong>On-Time, Every Time:</strong> We pride ourselves on punctuality, ensuring you depart and arrive exactly when you need to, so your plans stay on track.</li>
        </ol>

        <p><strong>Ready for a Smooth Ride?</strong></p>
        """;
    }

}