package suites.appsuites.operations.g_sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import base.BaseSuite;
import framework.input.Configuration;
import suites.appsuites.operations.Operations;

public class GSheetOperation extends Operations {

	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
	private static final String TOKENS_DIRECTORY_PATH = "./libs/tokens";
	private static final String APPLICATION_NAME = "Rocket Process Automation";
	private static final String CREDENTIALS_FILE_PATH = "./libs/resources/credentials.json";

	/**
	 * This is to handle timeout of the request
	 * 
	 * @param requestInitializer
	 * @return
	 */
	private HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
		return new HttpRequestInitializer() {

			@Override
			public void initialize(HttpRequest httpRequest) throws IOException {
				requestInitializer.initialize(httpRequest);
				httpRequest.setConnectTimeout(1 * 60000); // 3 minutes connect timeout
				httpRequest.setReadTimeout(1 * 60000); // 3 minutes read timeout

			}
		};
	}

	/**
	 * Creates an authorized Credential object.
	 *
	 * @param httpTransport
	 *            The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException
	 *             If the credentials.json file cannot be found.
	 */
	private Credential getCredentials(NetHttpTransport httpTransport) {

		Credential creds = null;

		try {

			// Load client secrets.
			InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
					clientSecrets, SCOPES)
							.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
							.setAccessType("offline").build();
			LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

			creds = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

			BaseSuite.log.info("Generated the credentials");

		} catch (Exception e) {
			BaseSuite.log.error("Exception: Error while authorizing ", e);
		}

		return creds;
	}

//	/**
//	 * Builds authorized client
//	 * 
//	 * @return Sheets object that helps in sheet operation
//	 */
//	public Sheets getSheetService() {
//
//		Sheets service = null;
//
//		try {
//			// Build a new authorized API client service.
//			NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//			Credential credential = getCredentials(httpTransport);
//
//			if (credential == null) {
//				BaseSuite.log.error("Could not authorize and unable to get credential");
//				return service;
//			} else {
//				service = new Sheets.Builder(httpTransport, JSON_FACTORY, setHttpTimeout(credential))
//						.setApplicationName(APPLICATION_NAME).build();
//			}
//			BaseSuite.log.debug("Successfully created sheet service");
//		} catch (Exception e) {
//			BaseSuite.log.error("Exception: Error while building client service " + e);
//		}
//
//		return service;
//	}

	public Sheets getSheetService() {

		Sheets service = null;

		try {

			GoogleCredentials credentials = ServiceAccountCredentials
					.fromStream(new FileInputStream("libs/resources/credentials.json"))
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

//	public void updateCell(String spreadsheetId, String sheetName, String cell, String value) throws IOException {
//
//		ValueRange body = new ValueRange().setValues(Arrays.asList(Arrays.asList(value)));
//
//		getSheetService().spreadsheets().values().update(spreadsheetId, sheetName + "!" + cell, body)
//				.setValueInputOption("RAW").execute();
//	}

	/**
	 * Update response and url in single call
	 * 
	 * @param response
	 * @param url
	 * @param rowNumber
	 */
	public void updateCell(String response, String url, int rowNumber) throws IOException {

		ValueRange body = new ValueRange().setValues(Arrays.asList(Arrays.asList(response, url)));

		getSheetService().spreadsheets().values().update(Configuration.getConfigProp("spreadsheetId"),
				"CreateAppSuite!E" + rowNumber + ":F" + rowNumber, body).setValueInputOption("RAW").execute();
	}

	/**
	 * get column count from the range
	 * 
	 * @param range-
	 *            string
	 * @return cloumnCount - int
	 */
	public int getColumnCount(String range) {

		// split the string
		String columnPart = range.split("!")[1];
		String endColumn = columnPart.split(":")[1].replaceAll("[0-9]", "");

		int number = 0;

		// count column count
		for (int i = 0; i < endColumn.length(); i++) {
			number = number * 26 + (endColumn.charAt(i) - 'A' + 1);
		}

		return number;
	}

	/**
	 * get sheetID from sheet name
	 * 
	 * @param sheetsService
	 * @param spreadSheetId
	 * @param sheetName
	 * @return sheetId - int
	 * @throws IOException
	 */
	public static int getSheetId(Sheets sheetsService, String spreadSheetId, String sheetName) throws IOException {

		Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadSheetId).execute();

		// get sheet id
		for (Sheet sheet : spreadsheet.getSheets()) {
			if (sheet.getProperties().getTitle().equals(sheetName)) {
				return sheet.getProperties().getSheetId();
			}
		}

		BaseSuite.log.error("Sheet not found: " + sheetName);
		throw new RuntimeException("Sheet not found: " + sheetName);
	}

	public Object[][] customInput(String spreadSheetId, String cellRange) {

		Object[][] data = null;

		try {

			GSheetOperation gSheetOperation = new GSheetOperation();

			Sheets sheetsService = gSheetOperation.getSheetService();

			String sheetName = "CAQHSuite";

			String range = sheetName + "!" + cellRange;

			BaseSuite.log.info("Reading data from range: " + range);

			ValueRange response = sheetsService.spreadsheets().values().get(spreadSheetId, range).execute();

			List<List<Object>> values = response.getValues();

			if (values == null || values.isEmpty()) {

				BaseSuite.log.error("No data found in Google Sheet.");

				return new Object[0][0];
			}

			int rowCount = values.size();
			int columnCount = values.get(0).size();

			data = new Object[rowCount][columnCount];

			for (int i = 0; i < rowCount; i++) {

				List<Object> row = values.get(i);

				for (int j = 0; j < columnCount; j++) {

					if (j < row.size()) {

						data[i][j] = String.valueOf(row.get(j));

					} else {

						data[i][j] = "";
					}
				}
			}

			BaseSuite.log.info("Successfully fetched data from Google Sheet.");

		} catch (Exception e) {

			BaseSuite.log.error("Error while reading Google Sheet data.", e);
		}

		return data;
	}
}
