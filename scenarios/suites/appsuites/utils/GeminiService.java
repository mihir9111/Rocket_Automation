package suites.appsuites.utils;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiService {

	// Use Gemini 1.5 Pro — Best Model
	private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

	private static final String API_KEY = "AIzaSyAnVKVeIaiusbnz4Dv2UAPDhx1nKi0ufWs";

	/**
	 * 
	 * @param appsCount
	 * @param useCase
	 * @param tech
	 * @param type
	 * @return
	 */
	public String generateGeminiPrompt(String appsCount, String useCase, String tech, String type) {
		// 2. Call the function to build the instruction string
		String dynamicPrompt = generateVibeCodingPrompt(appsCount, useCase, tech, type);

		Client client = Client.builder().apiKey("AIzaSyAXGbRKW-j9e38dAgYW0SuClWv7HeJAWn0").build();
		String modelId = "gemini-2.5-flash";

		// 3. Pass the result straight into your Gemini API client
		GenerateContentResponse response = client.models.generateContent(modelId, dynamicPrompt, null);
		String rocketNewPrompt = response.text();

		return rocketNewPrompt;
	}

	public String generateVibeCodingPrompt(String numberOfApps, String useCase, String technology, String appType) {

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
