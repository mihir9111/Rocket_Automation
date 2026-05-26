package suites.basesuites;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import base.BaseSuite;
import framework.input.Configuration;
import pages.CommonFunctions;
import pages.Strings;
import pages.SubModule;
import pages.login.Login;
import pages.login.home.Home;
import suites.appsuites.operations.Operations;
import suites.appsuites.utils.GSheetOperation;
import suites.appsuites.utils.GeminiService;

public class RocketBaseSuite extends BaseSuite implements SubModule {

	protected Login login;
	protected Home home;
	protected CommonFunctions commonFunctions;
	protected GSheetOperation gSheetOperation;
	protected Operations operations;
	protected GeminiService geminiService;

	protected static String spreadSheetId = Strings.emptyString;

	@BeforeSuite
	public void Login() {

		////////////////////////////////////
		/// for adding the test in report
		String methodName = "Login";
		setUpProjectTest(methodName);
		///////////////////////////////////

		login = createObject(LOGIN);

		String baseUrl = Configuration.getConfigProp("URL");
		String gmail = Configuration.getConfigProp("gmail");

		home = login.launchAndLogin(baseUrl, gmail);

		/////////////////////////////////////
		/// for ending the test in report
		tearDownProjectTest(methodName);
		/////////////////////////////////////
	}

	@BeforeClass
	public void intializeObjects() {

		// to be used in suite level
		commonFunctions = createObject(COMMON_FUNCTIONS);
		geminiService = new GeminiService();

		spreadSheetId = Configuration.getConfigProp("spreadsheetId");

	}

}
