package suites.appsuites.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import base.BaseSuite;
import framework.input.Configuration;

public class GSheetOperation {

	private static final String APPLICATION_NAME = "Rocket Process Automation";
	private static final String CREDENTIALS_FILE_PATH = "./libs/resources/credentialsForGSheet.json";

	/**
	 * Builds authorized client
	 * 
	 * @return Sheets object that helps in sheet operation
	 */
	public Sheets getSheetService() {

		Sheets service = null;

		try {

			GoogleCredentials credentials = ServiceAccountCredentials
					.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
					.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

			HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

			service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(),
					requestInitializer).setApplicationName(APPLICATION_NAME).build();

			BaseSuite.log.info("Google Sheet service initialized successfully.");

		} catch (Exception e) {

			BaseSuite.log.error("Failed to initialize Google Sheets service.", e);
		}

		return service;
	}

	/**
	 * Update response and url in single call
	 * 
	 * @param response
	 * @param url
	 * @param rowNumber
	 */
	public void updateCell(String response, String url, int rowNumber) {

		ValueRange body = new ValueRange().setValues(Arrays.asList(Arrays.asList(response, url)));

		try {
			getSheetService().spreadsheets().values()
					.update(Configuration.getConfigProp("spreadsheetId"),
							"CreateAppSuite!E" + rowNumber + ":F" + rowNumber, body)
					.setValueInputOption("RAW").execute();
		} catch (IOException e) {
			BaseSuite.log.error("Error ocurring while updating the cell value");
		}
	}

}
