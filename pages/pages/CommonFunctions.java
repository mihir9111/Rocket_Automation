package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BaseComponent;
import base.BaseSuite;
import framework.reporter.ScreenshotType;
import pages.login.Login_OR;

public class CommonFunctions extends BaseComponent implements Shared_OR {

	public static int explicitHighWait = 60;
	String prefixSuffix = "####";

	public CommonFunctions() {
	}

	/**
	 * This is used to indicate the start and end of scenario in the report.
	 * 
	 * @param message
	 *            to be logged
	 * @param takeScreenshot
	 *            boolean
	 */
	public void logScenario(String message, boolean... takeScreenshot) {

		boolean takeShot = false;
		if (takeScreenshot.length > 0) {
			takeShot = takeScreenshot[0];
		}
		if (message.contains("Started")) {
			RESULT.INFO("<b class='coresteplog'>" + prefixSuffix + " " + message + " " + prefixSuffix + "</b>",
					takeShot, ScreenshotType.browser);
		} else if (message.contains("Ended")) {
			RESULT.INFO("<b class='coresteplogE'>" + prefixSuffix + " " + message + " " + prefixSuffix + "</b>",
					takeShot, ScreenshotType.browser);
		} else {
			RESULT.INFO("<b>" + prefixSuffix + " " + message + " " + prefixSuffix + "</b>", takeShot,
					ScreenshotType.browser);
		}
	}

	/**
	 * 
	 * @param locator
	 * @param text
	 */
	public void setInnerText(By locator, String text) {

		try {

			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

			((JavascriptExecutor) driver).executeScript("arguments[0].innerText = arguments[1];", element, text);

			RESULT.PASS("Successfully set " + text + " in " + getLocatorName(locator), true, ScreenshotType.browser);

		} catch (Exception e) {
			RESULT.FAIL("Failed to set " + text + " in " + getLocatorName(locator), true, ScreenshotType.browser);
			BaseSuite.log.error("Exception while setting inner text", e);
		}
	}

	/**
	 * Logout from portal
	 */
	public void logOut() {
		logScenario("Started verifying logout functionality");

		if (isElementDisplayed(profileIcon)) {

			hoverToElement(profileIcon);

			clickAction(profileIcon);

			if (waitForElement(signOut, 30, WaitType.visibilityOfElementLocated)) {

				// check logout button is exists or not
				if (isElementExists(signOut)) {

					// click on Logout button
					javaScriptClick(signOut);

					// verification for the login button present after clicking on logout
					if (waitForElement(Login_OR.signIn, 60, WaitType.visibilityOfElementLocated, true)) {
						RESULT.PASS("Logout is performed successfully", true, ScreenshotType.browser);
					} else {
						RESULT.FAIL("Failed to logout from web portal", true, ScreenshotType.browser);
						exitApplication("Logout unsuccessful", true);
					}
				} else {
					RESULT.FAIL("Logout button is not present in this page", true, ScreenshotType.browser);
				}

			} else {
				RESULT.FAIL("Account menu is not opened after clicking on 'Account' icon", true,
						ScreenshotType.browser);
			}
		} else {
			RESULT.FAIL("Account button is not present in this page", true, ScreenshotType.browser);
		}
		logScenario("Ended verifying logout functionality");
	}

	public void hoverToElement(By locator) {
		try {

			WebElement element = getWebElement(locator);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).pause(Duration.ofSeconds(1)).perform();

			RESULT.PASS("Successfully hovered ", false, ScreenshotType.browser);
		} catch (Exception e) {
			BaseSuite.log.error("Error while hovering.", e);
		}
	}

}
