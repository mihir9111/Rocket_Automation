package pages;

import org.openqa.selenium.By;

public interface Shared_OR {

	// logout
	By profileIcon = By.xpath("//div[contains(@class,'profile')]");
	By signOut = By.xpath("//p[text()='Sign out']");

	// verifyNewWindow
	By body = By.tagName("body");

}
