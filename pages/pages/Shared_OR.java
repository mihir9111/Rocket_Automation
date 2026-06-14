package pages;

import org.openqa.selenium.By;

public interface Shared_OR {

	// logout
	By sideBar = By.xpath("//input[@id='sidebar-toggle']");
	By profileIcon = By.xpath("//div[contains(@class,'profile')]");
	By profileIcon2 = By.xpath("//div[contains(@class,'profile')]/div");
	By profileIcon3 = By.xpath("//div[contains(@class,'profile')]/div/img");
	By signOut = By.xpath("//p[text()='Sign out']");

	// verifyNewWindow
	By body = By.tagName("body");

	By links = By.tagName("a");

	By img = By.tagName("img");

}
