package suites.appsuites;

import java.util.List;

import org.apache.hc.client5.http.impl.Operations;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import base.BaseSuite;
import framework.input.Configuration;
import framework.reporter.ScreenshotType;
import pages.SubModule;
import suites.appsuites.operations.g_sheet.GSheetOperation;
import suites.appsuites.utils.GeminiService;
import suites.basesuites.RocketBaseSuite;

/**
 * Project specific base suite to be extended by every suite
 */
public class CreateAppSuite extends RocketBaseSuite implements SubModule {

	Operations operations;

	@Test(priority = 0, enabled = true, dataProvider = "customInput", description = "")
	public void generateApp(String numberOfApps, String useCase, String technology, String typeOfApplication,
			String rowNumber) {
		rowNumber = "3";
		// Step 4 — Call Gemini API
		GeminiService geminiService = new GeminiService();
		try {

//			String response = geminiService.generateGeminiPrompt(numberOfApps, useCase, technology, typeOfApplication);

//			gSheetOperation.updateCell(spreadSheetId, "CreateAppSuite", "E" + rowNumber, response);

//			String url = "";

//			if (home.selectApplicationType(useCase, typeOfApplication)) {
//
//				url = home.enterPromptAndGenerateURL(response);
//			}

//			gSheetOperation.updateCell(spreadSheetId, "CreateAppSuite", "F" + rowNumber, url);
			String response = "mihir";
			String url = "patel";
			gSheetOperation.updateCell(response, url, Integer.valueOf(rowNumber));

		} catch (Exception e) {
			BaseSuite.log.error("Error occuring while generating prompt.", e);
		}

	}

	@DataProvider(name = "customInput")
	public Object[][] customInput() {

		try {

			gSheetOperation = new GSheetOperation();

			Sheets sheetsService = gSheetOperation.getSheetService();

			String sheetName = CreateAppSuite.class.getSimpleName();
			String cellRange = Configuration.getConfigProp("range");

			String range = sheetName + "!" + cellRange;

			log.info("Reading data from range: " + range);

			ValueRange response = sheetsService.spreadsheets().values().get(spreadSheetId, range).execute();

			List<List<Object>> values = response.getValues();

			if (values == null || values.isEmpty()) {

				log.error("No data found in Google Sheet.");

				return new Object[0][0];
			}

			int rowCount = values.size();
			int columnCount = values.get(0).size();

			data = new Object[rowCount][columnCount + 1];

			for (int i = 0; i < rowCount; i++) {

				List<Object> row = values.get(i);

				for (int j = 0; j < columnCount; j++) {

					if (j < row.size()) {

						data[i][j] = String.valueOf(row.get(j));

					} else {

						data[i][j] = "";
					}
				}

				// Add Google Sheet row number
				data[i][columnCount] = String.valueOf(i + 2);
			}

			log.info("Successfully fetched data from Google Sheet.");

		} catch (Exception e) {

			log.error("Error while reading Google Sheet data.", e);
			RESULT.FAIL("Failed to get Input data from the google sheet", false, ScreenshotType.browser);
		}

		return data;
	}
}
