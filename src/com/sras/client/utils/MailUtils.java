package com.sras.client.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtils {

	public static boolean sendMail(String to, String subject, String body) {
		String[] recipients = new String[] { to };
		return sendMail(recipients, null, null, body, subject, null, false);
	}

	public static boolean sendMail(String to, String subject, String body,
			String fileName, Boolean htmlBody) {
		String[] recipients = new String[] { to };
		return sendMail(recipients, null, null, body, subject, fileName,
				htmlBody);
	}

	public static boolean sendMail(String[] recipients, String[] ccRecipients,
			String[] bccRecipients, String body, String subject,
			String fileName, boolean htmlBody) {
		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", ClientConstants.SMTP_HOST);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.debug", "false");
		properties.put("mail.smtp.ssl.enable", "true");

		try {
			// Get the default Session object.
			Session session = Session.getInstance(properties, new SocialAuth());
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(ClientConstants.FROM_ADDRESS));

			// Set To: header field of the header.
			if (recipients != null) {
				InternetAddress[] toAddresses = new InternetAddress[recipients.length];
				for (int i = 0; i < recipients.length; i++) {
					toAddresses[i] = new InternetAddress(recipients[i]);
				}
				message.setRecipients(Message.RecipientType.TO, toAddresses);
			}

			if (ccRecipients != null) {
				InternetAddress[] ccAddresses = new InternetAddress[ccRecipients.length];
				for (int j = 0; j < ccRecipients.length; j++) {
					ccAddresses[j] = new InternetAddress(ccRecipients[j]);
				}
				message.setRecipients(Message.RecipientType.CC, ccAddresses);
			}

			if (bccRecipients != null) {
				InternetAddress[] bccAddresses = new InternetAddress[bccRecipients.length];
				for (int j = 0; j < bccRecipients.length; j++) {
					bccAddresses[j] = new InternetAddress(bccRecipients[j]);
				}
				message.setRecipients(Message.RecipientType.BCC, bccAddresses);
			}

			// Set Subject: header field
			message.setSubject(subject);

			// Send the complete message parts
			if (htmlBody) {
				// Create the message part
				BodyPart messageBodyPart = new MimeBodyPart();

				// Fill the message
				messageBodyPart.setText(body);

				// Create a multipar message
				Multipart multipart = new MimeMultipart();

				// Set text message part
				multipart.addBodyPart(messageBodyPart);

				// Part two is attachment
				if (fileName != null) {
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(fileName);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(fileName);
					multipart.addBodyPart(messageBodyPart);
				}
				message.setContent(multipart, "text/html");
			} else {
				message.setText(body);
			}
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
			return true;
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		return false;
	}
}

class SocialAuth extends Authenticator {
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(ClientConstants.FROM_ADDRESS,
				ClientConstants.PASSWORD);
	}
}