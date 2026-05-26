package pages;

import pages.login.Login;
import pages.login.home.Home;
import pages.login.home.specificApp.SpecificApp;

public interface SubModule {

	String HOME = Home.class.getSimpleName();
	String LOGIN = Login.class.getSimpleName();
	String COMMON_FUNCTIONS = CommonFunctions.class.getSimpleName();
	String SPECIFIC_APP = SpecificApp.class.getSimpleName();
}
