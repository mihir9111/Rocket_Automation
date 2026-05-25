package suites.appsuites.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import base.BaseSuite;

public class OTPService {

//	/**
//	 * Get Rocket OTP after clicking Send button
//	 *
//	 * @param requestTime
//	 *            time captured before clicking send button
//	 * @return OTP
//	 */
//	public static String getOTP(long requestTime) {
//
//		String otp = Strings.emptyString;
//
//		try {
//
//			Gmail service = GmailService.getGmailService();
//
//			long startTime = System.currentTimeMillis();
//			long maxWait = 30000; // max wait 30 sec
//
//			while ((System.currentTimeMillis() - startTime) < maxWait) {
//
//				ListMessagesResponse response = service.users().messages().list("me")
//						.setQ("is:unread from:info@mail.rocket.new subject:Rocket").setMaxResults(5L).execute();
//
//				List<Message> messages = response.getMessages();
//
//				if (messages != null && !messages.isEmpty()) {
//
//					for (Message message : messages) {
//
//						Message fullMessage = service.users().messages().get("me", message.getId()).execute();
//
//						long mailTime = fullMessage.getInternalDate();
//
//						// Ignore emails before clicking Send
//						if (mailTime <= requestTime) {
//							continue;
//						}
//
//						String subject = Strings.emptyString;
//
//						for (MessagePartHeader header : fullMessage.getPayload().getHeaders()) {
//
//							if ("Subject".equalsIgnoreCase(header.getName())) {
//
//								subject = header.getValue();
//
//								break;
//							}
//						}
//
//						BaseSuite.log.info("Matched Subject : " + subject);
//
//						Matcher matcher = Pattern.compile("\\b\\d{6}\\b").matcher(subject);
//
//						if (matcher.find()) {
//
//							otp = matcher.group();
//
//							BaseSuite.log.info("OTP Found : " + otp);
//
//							return otp;
//						}
//					}
//				}
//
//				Thread.sleep(2000);
//			}
//
//		} catch (Exception e) {
//
//			BaseSuite.log.error("Error while getting OTP", e);
//		}
//
//		BaseSuite.log.warn("OTP not found");
//
//		return otp;
//	}

	/**
	 * getting otp with normal gmail reading
	 * 
	 * @return
	 */
	public static String getOTP() {

		String otp = "";

		try {

			Gmail service = GmailService.getGmailService();

			ListMessagesResponse response = service.users().messages().list("me").setQ("is:unread").setMaxResults(5L)
					.execute();

			List<Message> messages = response.getMessages();

			if (messages != null) {

				for (Message message : messages) {

					Message fullMessage = service.users().messages().get("me", message.getId()).execute();

					String snippet = fullMessage.getSnippet();

					BaseSuite.log.info(snippet);

					Pattern pattern = Pattern.compile("\\b\\d{6}\\b");

					Matcher matcher = pattern.matcher(snippet);

					if (matcher.find()) {

						otp = matcher.group();

						BaseSuite.log.info("OTP Found: " + otp);

						break;
					}
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return otp;
	}

//	/**
//	 * To getting otp from gmail
//	 * 
//	 * @param requestTime
//	 * @return
//	 */
//	public static String getOTP(long requestTime) {
//
//		String otp = Strings.emptyString;
//
//		try {
//
//			Gmail service = GmailService.getGmailService();
//
//			long startTime = System.currentTimeMillis();
//
//			long maxWait = 2 * 60 * 1000; // 2 minute
//
//			while ((System.currentTimeMillis() - startTime) < maxWait) {
//
//				ListMessagesResponse response = service.users().messages().list("me").setMaxResults(10L).execute();
//
//				List<Message> messages = response.getMessages();
//
//				if (messages != null) {
//
//					for (Message message : messages) {
//
//						Message fullMessage = service.users().messages().get("me", message.getId()).execute();
//
//						// Gmail internal timestamp
//						long mailTime = fullMessage.getInternalDate();
//
//						// Ignore old mails
//						if (mailTime <= requestTime) {
//
//							continue;
//						}
//
//						String snippet = fullMessage.getSnippet();
//
//						Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
//
//						Matcher matcher = pattern.matcher(snippet);
//
//						if (matcher.find()) {
//
//							otp = matcher.group();
//
//							BaseSuite.log.info(otp + " otp found in last gmail.");
//
//							return otp;
//						}
//					}
//				}
//
//				Thread.sleep(5000);
//			}
//
//		} catch (Exception e) {
//			BaseSuite.log.error("Error getting OTP", e);
//		}
//
//		return otp;
//	}

//	/**
//	 * To get OTP from Gmail
//	 * 
//	 * @param requestTime
//	 * @return OTP
//	 */
//	public static String getOTP(long requestTime) {
//
//		String otp = Strings.emptyString;
//
//		try {
//
//			Gmail service = GmailService.getGmailService();
//
//			long startTime = System.currentTimeMillis();
//			long maxWait = 2 * 60 * 1000; // 2 minutes
//
//			while ((System.currentTimeMillis() - startTime) < maxWait) {
//
//				ListMessagesResponse response = service.users().messages().list("me").setMaxResults(10L).execute();
//
//				List<Message> messages = response.getMessages();
//
//				if (messages != null && !messages.isEmpty()) {
//
//					for (Message message : messages) {
//
//						Message fullMessage = service.users().messages().get("me", message.getId()).execute();
//
//						long mailTime = fullMessage.getInternalDate();
//
//						BaseSuite.log.info("Mail Time : " + mailTime);
//						BaseSuite.log.info("Request Time : " + requestTime);
//
//						// Ignore old emails
//						if (mailTime <= requestTime) {
//							continue;
//						}
//
//						String body = getMessageBody(fullMessage);
//
//						BaseSuite.log.info("Email Body : " + body);
//
//						Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
//
//						Matcher matcher = pattern.matcher(body);
//
//						if (matcher.find()) {
//
//							otp = matcher.group();
//
//							BaseSuite.log.info("OTP Found : " + otp);
//
//							return otp;
//						}
//					}
//				}
//
//				Thread.sleep(5000);
//			}
//
//		} catch (Exception e) {
//
//			BaseSuite.log.error("Error getting OTP", e);
//		}
//
//		BaseSuite.log.warn("OTP not found");
//
//		return otp;
//	}
//
//	/**
//	 * Get complete email body
//	 * 
//	 * @param message
//	 * @return email body
//	 */
//	private static String getMessageBody(Message message) {
//
//		try {
//
//			MessagePart payload = message.getPayload();
//
//			// Single-part email
//			if (payload.getBody() != null && payload.getBody().getData() != null) {
//
//				byte[] emailBytes = Base64.getUrlDecoder().decode(payload.getBody().getData());
//
//				return new String(emailBytes, StandardCharsets.UTF_8);
//			}
//
//			// Multi-part email
//			if (payload.getParts() != null) {
//
//				for (MessagePart part : payload.getParts()) {
//
//					if (part.getBody() != null && part.getBody().getData() != null) {
//
//						byte[] emailBytes = Base64.getUrlDecoder().decode(part.getBody().getData());
//
//						return new String(emailBytes, StandardCharsets.UTF_8);
//					}
//				}
//			}
//
//		} catch (Exception e) {
//
//			BaseSuite.log.error("Error reading email body", e);
//		}
//
//		return Strings.emptyString;
//	}

}
