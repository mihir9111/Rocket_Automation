package pages.login.home.specificApp;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import framework.reporter.ScreenshotType;
import framework.setup.SetUp;
import pages.login.home.Home;

public class SpecificApp extends Home {

	/**
	 * Verify new application's title, body, error on page, links
	 * 
	 * @param url
	 */
	public void verifyNewApplication(String url) {

		launchApplication(url);

		pause(5);

		if (waitForPageLoad()) {

			verifyTitle();

			verifyBody();

			verifyNoErrorPage();

			verifyLinks();

			verifyImages();

			verifyJavaScriptErrors();
		}
	}

	/**
	 * Verify page title exists
	 */
	private void verifyTitle() {

		String title = SetUp.driver.getTitle();

		if (title.isBlank()) {
			RESULT.PASS("Successfully verified title of website is " + title, true, ScreenshotType.browser);
		} else {
			RESULT.WARNING("Failed to verify title of website. Title is empty.", true, ScreenshotType.browser);
		}
	}

	/**
	 * Verify body loaded
	 */
	private void verifyBody() {

		if (isElementDisplayed(body)) {
			RESULT.PASS("Successfully displayed body of website.", true, ScreenshotType.browser);
		} else {
			RESULT.WARNING("Failed to verify body of website", true, ScreenshotType.browser);
		}
	}

	/**
	 * Verify error page not displayed
	 */
	private void verifyNoErrorPage() {

		String pageSource = getCurrentPageSource();

		if (pageSource != null) {
			pageSource = pageSource.toLowerCase();

			boolean errorFound = pageSource.contains("404") || pageSource.contains("500")
					|| pageSource.contains("page not found") || pageSource.contains("application error")
					|| pageSource.contains("internal server error");

			if (errorFound) {
				RESULT.WARNING("Page contains error", true, ScreenshotType.browser);
			} else {
				RESULT.PASS("Successfully verify pages has no error", true, ScreenshotType.browser);
			}
		}

	}

	/**
	 * Verify links
	 */
	private void verifyLinks() {

		if (isElementDisplayed(links)) {

			List<WebElement> link = getList(links);

			int linkSize = link.size();

			if (linkSize == 0) {
				RESULT.WARNING("Zero link found on website", true, ScreenshotType.browser);
			} else {
				RESULT.PASS("Links found : " + linkSize + " on website", true, ScreenshotType.browser);
			}

		}
	}

	/**
	 * Verify images
	 */
	private void verifyImages() {

		if (isElementDisplayed(img)) {

			List<WebElement> image = getList(img);

			int imageSize = image.size();

			if (imageSize == 0) {
				RESULT.WARNING("Zero image found on website", true, ScreenshotType.browser);
			} else {
				RESULT.PASS("Images found : " + imageSize + " on website", true, ScreenshotType.browser);
			}

		}
	}

	/**
	 * Verify JavaScript errors
	 */
	private void verifyJavaScriptErrors() {

		LogEntries logs = SetUp.driver.manage().logs().get(LogType.BROWSER);

		for (LogEntry log : logs) {

			if (log.getLevel().toString().contains("SEVERE")) {

				RESULT.WARNING("JS Error : " + log.getMessage(), false, ScreenshotType.browser);
			}
		}
	}
}
