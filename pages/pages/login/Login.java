package pages.login;

import framework.reporter.ScreenshotType;
import pages.CommonFunctions;
import pages.SubModule;
import pages.login.home.Home;
import suites.appsuites.utils.OTPService;

public class Login extends CommonFunctions implements Login_OR {

	/**
	 * Method to launch and login into the application.
	 * 
	 * @param url
	 *            - url of portal
	 * @param email
	 *            - email to login
	 * @return object of home if logged in else null
	 */
	public Home launchAndLogin(String url, String email) {

		logScenario("Started launcing the " + url + " url");

		Home home = null;

		// launch Rocket application
		launchApplication(url);

		if (isElementDisplayed(signIn)) {

			// click on sign in button
			javaScriptClick(signIn);

			// set gmail and click on continue button
			setValue(inputEmail, email);
			javaScriptClick(continueBtn);

			pause(5);

			// getting otp from gmail
			String otp = OTPService.getOTP();

			// set the otp
			setValue(inputOTP, otp);
			click(continueBtn);

		}

		if (waitForElement(Home.buildBtn, 60, WaitType.visibilityOfElementLocated, true)) {

			RESULT.PASS("Successfully logged into Rocket Portal.", true, ScreenshotType.browser);

			home = createObject(SubModule.HOME);
		}

		logScenario("Ended launcing the " + url + " url");

		if (home == null) {
			exitApplication("Login unsuccessful", true);
		}

		// return to Home Page
		return home;

	}// end of launch and login

}
