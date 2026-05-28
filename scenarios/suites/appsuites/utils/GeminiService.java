package suites.appsuites.utils;

import java.util.List;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import framework.input.Configuration;
import pages.Strings;

public class GeminiService {

	private static final String API_KEY = Configuration.getConfigProp("geminiAPIKey");
	private static final String MODEL_ID = Configuration.getConfigProp("geminiModel");

	/**
	 * To generate the prompt from gemini
	 * 
	 * @param appsCount
	 *            - number of application
	 * @param useCase
	 *            - web app, mobile app
	 * @param tech
	 *            - next.js, react-native
	 * @param type
	 *            - which kind of application is that like Gym, Food, E-commerce
	 * @return prompt to enter in rocket website
	 */
	public String generateGeminiPrompt(String appsCount, String useCase, String tech, String type) {

		String rocketNewPrompt = Strings.emptyString;

		// 1. Call the function to build the instruction string
		String dynamicPrompt = generateVibeCodingPrompt(appsCount, useCase, tech, type);

		Client client = Client.builder().apiKey(API_KEY).build();

		// 3. Pass the result straight into your Gemini API client
		GenerateContentResponse response = client.models.generateContent(MODEL_ID, dynamicPrompt, null);
		rocketNewPrompt = response.text();

		return rocketNewPrompt;
	}

	/**
	 * To generate the prompt from gemini
	 * 
	 * @param appsCount
	 *            - number of application
	 * @param useCase
	 *            - web app, mobile app
	 * @param tech
	 *            - next.js, react-native
	 * @param type
	 *            - which kind of application is that like Gym, Food, E-commerce
	 * @return prompt to enter in rocket website
	 */
	public String generateSummary(String requirment, List<String> buttons, List<String> links, String bodyText) {

		String generatedSummary = Strings.emptyString;

		// 1. create the client
		Client client = Client.builder().apiKey(API_KEY).build();

		// 2. generate the prompt
		String websiteDataWithPrompt = generatedPromptForSummaryOfApp(requirment, buttons, links, bodyText);

		// 3. Pass the website data with prompt to analyze into your Gemini API client
		GenerateContentResponse summary = client.models.generateContent(MODEL_ID, websiteDataWithPrompt, null);
		generatedSummary = summary.text();

		return generatedSummary;
	}

	/**
	 * Generate the prompt for summary of application
	 * 
	 * @param requirement
	 * @param buttons
	 * @param links
	 * @param bodyText
	 * @return
	 */
	private String generatedPromptForSummaryOfApp(String requirement, List<String> buttons, List<String> links,
			String bodyText) {

		return String.format(
				"Requirement:\n" + "%s\n\n" + "Discovered Buttons:\n" + "%s\n\n" + "Discovered Links:\n" + "%s\n\n"
						+ "Page Content:\n" + "%s\n\n" + "You are an expert AI QA auditor.\n\n"
						+ "Analyze the generated application and compare it against the requirement.\n\n"
						+ "Validation Rules:\n" + "1. Detect which features are implemented.\n"
						+ "2. Detect missing functionality.\n" + "3. Detect possible broken flows.\n"
						+ "4. Identify whether important features appear functional.\n"
						+ "5. Calculate overall requirement match percentage.\n\n" + "Return STRICT JSON only.\n"
						+ "JSON format:\n" + "{\n" + "\"score\":\"85%%\",\n" + "\"working_features\":[],\n"
						+ "\"missing_features\":[],\n" + "\"broken_features\":[],\n" + "\"summary\":\"\"\n" + "}",
				requirement, buttons, links, bodyText);
	}

	/**
	 * Generating the prompt for gemini
	 * 
	 * @param numberOfApps
	 *            - number of application
	 * @param useCase
	 *            - web app, mobile app
	 * @param technology
	 *            - next.js, react-native
	 * @param appType
	 *            - which kind of application is that like Gym, Food, E-commerce
	 * @return prompt for enter in gemini
	 */
	private String generateVibeCodingPrompt(String numberOfApps, String useCase, String technology, String appType) {

		return String.format(

				"You are an expert product architect and UI/UX designer.\n\n"

						+ "Analyze the application type and intelligently determine the most important features users expect.\n\n"

						+ "Application details:\n" + "- Number of Apps: %s\n" + "- Use Case: %s\n"
						+ "- Technology: %s\n" + "- Application Type: %s\n\n"

						+ "Generate an application prompt suitable for AI vibe-coding tools like Rocket.new.\n\n"

						+ "Required structure:\n"

						+ "1. Start with a short application description.\n"

						+ "2. Include 5–8 core features inferred from the application type.\n"

						+ "3. Include pages/screens required.\n"

						+ "4. Include user workflow and interactions.\n"

						+ "5. Include modern UI/UX expectations.\n"

						+ "6. Include authentication/dashboard/search/filtering/admin features if applicable.\n\n"

						+ "Automation Rules:\n" + "- Return only prompt content.\n"
						+ "- No explanation before or after.\n" + "- Keep response between 4–8 lines.\n"
						+ "- Do not use markdown code blocks or backticks.",

				numberOfApps, useCase, technology, appType);
	}

}
