package pages.login.home;

import org.openqa.selenium.By;

import framework.reporter.ScreenshotType;
import pages.login.Login;

public class Home extends Login implements Home_OR {

	/**
	 * To creating new task and select application tab
	 * 
	 * @param useCase
	 *            - for selecting application tab
	 * @param typeOfApplication
	 *            - if type is e-commerce and select e-commerce tab
	 * @return true if selected successfully else false
	 */
	public boolean selectApplicationType(String useCase, String typeOfApplication) {

		logScenario("Started creating new task and selecting tab");

		boolean selected = false;

		if (typeOfApplication.equalsIgnoreCase("e-commerce")) {
			useCase = "E-commerce";
		}

		By application = getLocator(applicationTab, useCase.toLowerCase());

		if (isElementDisplayed(newTask, true)) {
			javaScriptClick(newTask);

			if (waitForElement(buildBtn, 5, WaitType.visibilityOfElementLocated, true)) {
				javaScriptClick(buildBtn);

				if (!isElementDisplayed(application)) {
					application = getLocator(applicationTab, "Website");
				}

				javaScriptClick(application);

				if (waitForElement(promptInputField, 5, WaitType.visibilityOfElementLocated, true)) {
					RESULT.PASS("Selected application type - " + useCase, true, ScreenshotType.browser);
					selected = true;
				}
			}
		}

		logScenario("Ended creating new task and selecting tab");

		return selected;
	}

	/**
	 * To generate url after entering prompt and click on send button
	 * 
	 * @param definition
	 *            - to enter in prompt input field
	 * @return url of generated application
	 */
	public String enterPromptAndGenerateURL(String definition) {

		logScenario("Started entering prompt and generating url");

		String url = "";

		// enter prompt and click on send button
		setInnerText(promptInputField, definition);
		pause(1);
		click(sendPromptBtn);

		RESULT.PASS("Prompt submitted successfully", true, ScreenshotType.browser);

		pause(2);

		long startTime = System.currentTimeMillis();

		long maxWait = 20 * 60 * 1000; // 20 minutes

		while ((System.currentTimeMillis() - startTime) < maxWait) {

			// Launch/Preview success path
			if (isElementDisplayed(previewBtn, false)) {

				RESULT.INFO("Preview button found", true, ScreenshotType.browser);

				break;
			}

			// Submit appears
			if (isElementDisplayed(submitBtn, false)) {

				clickWithPageLoad(submitBtn);

				RESULT.INFO("Clicked Submit button", true, ScreenshotType.browser);

				pause(3);
			}

			// Skip appears
			if (isElementDisplayed(skipBtn, false)) {

				clickWithPageLoad(skipBtn);

				RESULT.INFO("Clicked Skip button", true, ScreenshotType.browser);

				pause(3);
			}

			// Without template appears
			if (isElementDisplayed(withoutTemplate, false)) {

				clickWithPageLoad(withoutTemplate);

				RESULT.INFO("Clicked Without Template", true, ScreenshotType.browser);

				pause(3);
			}

			pause(10);
		}

		// Launch flow
		if (waitForElement(previewBtn, explicitHighWait, WaitType.visibilityOfElementLocated, true)) {

			if (waitForElement(launchBtn, explicitHighWait, WaitType.visibilityOfElementLocated, true)) {

				if (isElementDisplayed(launchBtn)) {
					click(launchBtn);
				}

				pause(1);

				if (waitForElement(rocketHostedRadioBtn, explicitHighWait, WaitType.visibilityOfElementLocated,
						false)) {
					click(rocketHostedRadioBtn);
				}

				pause(1);

				if (isElementDisplayed(launchBtnInRocketHostedRadio)) {
					click(launchBtnInRocketHostedRadio);
				}

				if (waitForElement(nextBtn, explicitHighWait, WaitType.visibilityOfElementLocated, false)) {
					click(nextBtn);
				}

				if (waitForElement(updateBtn, 600, WaitType.visibilityOfElementLocated, true)) {

					if (isElementEnabled(urlLink)) {

						url = getTextWebelement(urlLink);

						RESULT.PASS("URL generated : " + url, true, ScreenshotType.browser);
					}
				}

			}
		}

		logScenario("Ended entering prompt and generating url");

		return url;
	}

}
