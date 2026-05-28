package pages.data;

import java.util.List;

public class AiValidationResponse {

	public String score;

	public List<String> working_features;

	public List<String> missing_features;

	public List<String> broken_features;

	public String summary;

}
