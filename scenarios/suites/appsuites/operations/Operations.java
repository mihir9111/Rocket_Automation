package suites.appsuites.operations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.Logger;

import base.BaseComponent;
import base.BaseSuite;
import framework.Cyfer;
import framework.email.EmailSection;
import framework.input.Configuration;

public class Operations extends BaseComponent {

	String mailSubject = "Summary of Rocket Process Automation - ";
	private Logger log = BaseSuite.log;

	/**
	 * To send the start execution email
	 */
	public void sendBeforeExecutionEmail() {

		EmailSection emailSection = new EmailSection();
		mailSubject = mailSubject + getTodayDate();
		sendEmail(mailSubject, emailSection.beforeExecutionWithoutData("Rocket Process Automation Execution Started"),
				Configuration.getConfigProp("toemail"));

	}

	/**
	 * To send a email
	 * 
	 * @param emailSubject
	 * @param emailBody
	 * @param toEmails
	 */
	public void sendEmail(String emailSubject, String emailBody, String toEmails) {

		log.debug("Started sending email");

		// get the configuration property for sending the email
		String hostName = Configuration.getConfigProp("host");
		String userName = Configuration.getConfigProp("emailusername");
		String bccEmail = Configuration.getDevConfigProp("bccEmails");

		// check if 'key' available to encrypt the password
		String encryptionKey = Configuration.getConfigProp("key");
		String password;
		if (encryptionKey != null && !encryptionKey.trim().isEmpty()) {
			password = Cyfer.decrypt(Configuration.getConfigProp("emailpassword"), encryptionKey);
		} else {
			password = Configuration.getConfigProp("emailpassword");
		}

		// Create the email message
		HtmlEmail email = new HtmlEmail();
		// for setting ssl protol
		System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
		email.setHostName(hostName);
		email.setSmtpPort(587);
//		will take 25 port as setting ssl true
//		email.setAuthenticator(new DefaultAuthenticator(userName, password));
//		email.setSSLOnConnect(true);
		email.setStartTLSEnabled(true);

		try {
			// set the email to and from
			email.setFrom(userName);

			if (toEmails != null && !toEmails.isEmpty()) {
				email.addTo(toEmails.split(","));
			}

			if (bccEmail != null && !bccEmail.isEmpty()) {
				email.addBcc(bccEmail.split(","));
			}

			// set the email subject
			email.setSubject(emailSubject);

			// set the email body
			email.setHtmlMsg(emailBody);

			// send the email
			email.send();

			log.debug("Email sent successfully");

		} catch (EmailException e) {
			log.error("Exception occured while sending a email", e);
		}

		log.debug("Ended sending email");
	}

	/**
	 * Send execution summary mail
	 */
	public void sendExecutionMail(String toEmail, boolean isProviderNotExist) {

		log.debug("Started making email template");

		String reportName = RESULT.getReportName();
		String reportURL = Configuration.getConfigProp("reportPortalURL").split("uploads")[0] + "Central?selected="
				+ reportName;

		String googleSheetURL = Configuration.getConfigProp("googleSheetUrl");

		String today = getTodayDate();
		// Check if report URL is available
		boolean hasReport = reportName != null && !reportName.trim().isEmpty();

		String emailTemplate =

				"<div style='margin:0;padding:40px;background:#f4f6fb;font-family:Verdana,Geneva,sans-serif'>"

						// Card Container
						+ "<div style='max-width:650px;margin:auto;background:#ffffff;border-radius:12px;"
						+ "box-shadow:0 6px 20px rgba(0,0,0,0.08);overflow:hidden'>"

						// Header (dynamic title)
						+ "<div style='background:linear-gradient(90deg,#4A00E0,#8E2DE2);padding:24px;color:white'>"
						+ "<div style='font-size:22px;font-weight:600;text-align:center'>"
						+ (isProviderNotExist ? "Execution Summary"
								: hasReport ? "Execution Report" : "Execution Summary")
						+ "</div></div>"

						// Body
						+ "<div style='padding:35px'>"

						// Dynamic message
						+ "<p style='font-size:16px;color:#333;margin-bottom:25px'>" + "Hello Team,<br><br>"
						+ "Test execution completed on <b>" + today + "</b>. "
						+ (isProviderNotExist ? "There is no provider for execution."
								: hasReport ? "You can review the detailed results in the report below."
										: "The detailed report is not uploaded.")
						+ "</p>"

						// Report Button (only if available)
						+ (isProviderNotExist ? ""
								: hasReport
										? "<div style='text-align:center;margin:30px 0'>"
												+ reportPortalButton(reportURL, "View Execution Report") + "</div>"
										: "")

						// Google Sheet Section (message changes slightly)
						+ "<div style='font-size:16px;color:#333'>"
						+ (isProviderNotExist ? "You can review the summarized results in the Google Sheet below:"
								: hasReport ? "The summarized results are also available in the Google Sheet:"
										: "You can review the summarized results in the Google Sheet below:")
						+ "</div>"

						// Google Sheet Button (always present)
						+ "<div style='text-align:center;margin:30px 0'>"
						+ googleSheetButton(googleSheetURL, "Open Execution Google Sheet") + "</div>"

						+ "</div></div></div>";

		log.debug("Ended making email template");

		sendEmail(mailSubject, emailTemplate, toEmail);
	}

	/**
	 * Creates a simple button for a URL (e.g., Google Sheet)
	 * 
	 * @param sheetUrl
	 *            The URL of the Google Sheet
	 * @param buttonText
	 *            The text to display on the button
	 * @return HTML string for the button
	 */
	public String googleSheetButton(String sheetUrl, String buttonText) {

		String htmlContent = "<a href='" + sheetUrl + "' " + "style='text-decoration:none;border:1px solid #dadce0;"
				+ "padding:8px 12px;border-radius:8px;display:inline-flex;" + "align-items:center'>"

				+ "<img src='https://ssl.gstatic.com/docs/doclist/images/mediatype/icon_1_spreadsheet_x16.png' "
				+ "style='width:20px;height:20px;margin-right:8px'>"

				+ "<span style='font-family:Arial;font-size:14px;color:#202124'>" + buttonText + "</span>"

				+ "</a>";

		return htmlContent;
	}

	/**
	 * Creates a simple button for a report portal
	 * 
	 * @param report
	 *            portal url
	 *            The URL of the report portal
	 * @param buttonText
	 *            The text to display on the button
	 * @return HTML string for the button
	 **/
	public String reportPortalButton(String url, String buttonText) {

		String htmlContent = "<a href='" + url + "' " + "style='text-decoration:none;" + "display:inline-block;"
				+ "padding:12px 12px;" + "font-family:Verdana, sans-serif;" + "font-size:18px;" + "font-weight:600;"
				+ "color:black;" + "border-radius:10px;" + "border:3px solid transparent;"
				+ "background: linear-gradient(white, white) padding-box, "
				+ "linear-gradient(90deg, purple, blue) border-box;" + "text-align:center;'>" + buttonText + "</a>";

		return htmlContent;
	}

	/**
	 * retrun today date
	 * 
	 * @return String
	 *         - return today date.
	 */
	public String getTodayDate() {

		// convert date in ist.
		ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		LocalDateTime istTime = zonedDateTime.toLocalDateTime();

		// format date in dd MMM yyyy
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		String today = istTime.format(formatter);

		return today;
	}
}
