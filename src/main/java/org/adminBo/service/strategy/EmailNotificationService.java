package org.adminBo.service.strategy;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.adminBo.contact.INotificationService;
import org.adminBo.dto.payment.PurchaseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Qualifier("emailNotificationService")

public class EmailNotificationService implements INotificationService {

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, PurchaseDTO purchase) {

        try {

            String html = buildPurchaseMessage(purchase);

            MimeMessage mimeMessage =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Compra realizada correctamente - DannySystem");
            helper.setText(html, true);

            mailSender.send(mimeMessage);

            System.out.println("EMAIL SENT TO: " + to);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildPurchaseMessage(PurchaseDTO purchase) {

        StringBuilder sb = new StringBuilder();
        String paymentMethod = purchase.getPaymentMethod().getValue();

        sb.append("<h2>¡Gracias por tu compra con ") .append(paymentMethod).append("!</h2>");
        sb.append("<p>Fecha: ").append(purchase.getDate()) .append("</p>");
        sb.append("<p>Total: ").append(purchase.getTotal()).append(" ")
                .append(purchase.getCurrency()) .append("</p>");
        sb.append("<table border='1' style='border-collapse: collapse;'>");
        sb.append("""
                <tr>
                    <th>Producto</th>
                    <th>Precio</th>
                    <th>Cantidad</th>
                    <th>Subtotal</th>
                </tr>
                """);

        purchase.getItems().forEach(item -> {
            sb.append("<tr>");
            sb.append("<td>") .append(item.getName()).append("</td>");
            sb.append("<td>") .append(item.getPrice()).append("</td>");
            sb.append("<td>") .append(item.getQuantity()).append("</td>");
            sb.append("<td>") .append(item.getSubtotal()).append("</td>");
            sb.append("</tr>");
        });

        sb.append("</table>");
        return sb.toString();
    }
}