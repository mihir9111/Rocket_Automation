package pages.login;

import org.openqa.selenium.By;

import pages.Shared_OR;

public interface Login_OR extends Shared_OR {

	// sign in buttons
	By signIn = By.xpath("//button[text()='Sign in']");

	By inputEmail = By.xpath("//input[@name='email']");
	By continueBtn = By.xpath("//p[text()='Continue']/ancestor::button[@type='submit']");
	By inputOTP = By.xpath("//input[@name='otp']");
}
