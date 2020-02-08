package jugistanbul.smtp;

import jugistanbul.entity.Speaker;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@Component
public class SendEmail
{

    private static final String FROM = "huseyin@javaday.istanbul";
    private static final String MESSAGE = "Dear %s,\n" +
            "\n" +
            "We are sorry to inform you that your talk, %s, wasn't selected for Java Day Istanbul.\n" +
            "\n" +
            "We had a lot of great submissions to our CFP, including yours, and the decision making process was not easy.\n" +
            "\n" +
            "Hopefully we will still you see at Java Day Istanbul.\n" +
            "\n" +
            "Thank you again.\n" +
            "\n" +
            "Java Day Istanbul Organizers\n";

    private final JavaMailSender javaMailSender;

    public SendEmail(final JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public String sendEmail(final Speaker speaker) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(speaker.getMail());
        msg.setFrom(FROM);
        msg.setSubject("Your talk wasn't selected for Java Day Istanbul");
        msg.setText(String.format(MESSAGE, speaker.getName(), speaker.getTitle()));

        javaMailSender.send(msg);
        return speaker.getMail();
    }
}
