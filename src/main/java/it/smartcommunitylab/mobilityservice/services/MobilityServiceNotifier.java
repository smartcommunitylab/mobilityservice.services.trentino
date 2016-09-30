package it.smartcommunitylab.mobilityservice.services;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MobilityServiceNotifier {

	@Value("${mail.host?: null}")
	private String mailHost;
	@Value("${mail.port?: null}")
	private String mailPort;
	@Value("${mail.user?: null}")
	private String mailUser;
	@Value("${mail.password?: null}")
	private String mailPassword;
	@Value("${mail.from?: null}")
	private String mailFrom;
	@Value("${mail.to?: null}")
	private String mailTo;	
	
	@Value("${mail.enabled?:false}")
	private String enabled;
	
	private static final Logger logger = LoggerFactory.getLogger(MobilityServiceNotifier.class);

	public MobilityServiceNotifier(){
	}
	
	public Boolean sendServiceNotification(String subject, String msg) {
		try {
			if (!Boolean.parseBoolean(enabled)) {
				return null;
			}

			PasswordAuthentication pa = new PasswordAuthentication(mailUser, mailPassword);

			Properties properties = System.getProperties();
			properties.put("mail.transport.protocol", "smtp");
			properties.put("mail.smtp.auth", "true");
			properties.setProperty("mail.smtp.host", mailHost);
			properties.setProperty("mail.smtp.port", mailPort);
			properties.put("mail.smtp.starttls.enable","true"); 
			properties.put("mail.smtp.EnableSSL.enable","true");			

			Session session = Session.getInstance(properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					try {
						return new PasswordAuthentication(mailUser, mailPassword);
					} catch (Exception e) {}
					return null;
				}
			});

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));
			String to[] = mailTo.split(",");
			for (String t: to) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(t));
			}
			message.setSubject(subject);
			message.setText(msg);
			
			Transport.send(message);
			logger.debug("Email sent.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
}
