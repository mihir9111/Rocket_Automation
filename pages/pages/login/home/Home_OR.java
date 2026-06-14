package pages.login.home;

import org.openqa.selenium.By;

import pages.Shared_OR;

public interface Home_OR extends Shared_OR {

	// new task at left panel
	By newTask = By.xpath("//p[text()='New task']/ancestor::a");

	// input fields
	By buildBtn = By.xpath("//span[text()='Build']/ancestor::div[@role='button']");
	By applicationTab = By.xpath(
			"//p[translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='%s']/ancestor::button");
	By promptInputField = By.xpath("//p[@data-placeholder='Describe what you need built. Rocket handles the rest.']");
	By sendPromptBtn = By.xpath("//button[@type='submit' and @aria-disabled='false']");

	// after clicking on send button
	By skipBtn = By.xpath("//p[contains(text(),'want to improve my prompt further')]/ancestor::button");
	By previewBtn = By.xpath("//p[text()='Preview']/ancestor::button");
	By submitBtn = By.xpath("//p[text()='Submit']/ancestor::button");
	By withoutTemplate = By.xpath("//p[text()='Continue without a template']/ancestor::button");

	By launchBtn = By.xpath("//p[text()='Launch']/ancestor::button[@aria-disabled='false']");
	By rocketHostedRadioBtn = By.xpath("//p[text()='Rocket hosted domain']");
	By nextBtn = By.xpath("//p[text()='Next']/ancestor::button[@aria-disabled='false']");
	By urlLink = By.xpath("//a[@rel='noopener noreferrer']/p");
	By updateBtn = By.xpath("//p[text()='Update']/ancestor::button[@aria-disabled='false']");
	By launchBtnInRocketHostedRadio = By
			.xpath("//div[@role='menu']//p[text()='Launch']/ancestor::button[@aria-disabled='false']");

}
