package parser;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.*;

public class JishoParser implements Parser {
	private ArrayList<String> commonWords = new ArrayList<>();
	private ArrayList<String> commonReadings = new ArrayList<>();
	private ArrayList<String> commonMeanings = new ArrayList<>();

	public void parse(ArrayList<String> lines) throws IOException {
		int progress = 0;
		for (String l : lines) {
			progress++;
			System.out.println(progress + "/" + lines.size());

			// FOR TESTING PURPOSES
			if(progress == 30) {
				break;
			}

			// Open a URL using Jisho's API.
			String encodedWord = URLEncoder.encode(l, "UTF-8");
			URL jishoAPI = new URL("https://jisho.org/api/v1/search/words?keyword=" + encodedWord);
			// System.out.println(jishoAPI); // -- Test to see if the URL is properly
			// sanitized

			// Scan in the URL and pass it to a JSONObject
			Scanner scan;
			String data;
			try {
				scan = new Scanner(jishoAPI.openStream());
				data = new String();
			} catch (Exception e) {
				continue;
			}
			while (scan.hasNext()) {
				data += scan.nextLine();
			}
			scan.close();
			JSONObject apiData = new JSONObject(data);

			// Use the JSONObject data to determine whether or not a word is common.
			// If it is, add the Word, Reading, and Meaning to their own respective
			// ArrayLists
			try {
				if (isCommon(apiData)) {

					try {
						// Sometimes a word is not represented by a kanji, therefore we must
						// use a different set of instructions for word addition
						commonWords.add(getWord(apiData));
					} catch (Exception e) {
						commonWords.add(l);
						commonReadings.add(getReading(apiData));
						commonMeanings.add(getMeaning(apiData));
						continue;
					}
					commonReadings.add(getReading(apiData));
					commonMeanings.add(getMeaning(apiData));
				}
			} catch (Exception e) {
				continue;
			}
		}

		for (int i = 0; i < commonWords.size(); i++) {
			System.out.println("Word: " + commonWords.get(i));
			System.out.println("Reading: " + commonReadings.get(i));
			System.out.println("Meaning: " + commonMeanings.get(i));
		}

		System.out.println("Words: " + commonWords.size());
		System.out.println("Readings: " + commonReadings.size());
		System.out.println("Meanings: " + commonMeanings.size());
	}

	/**
	 * Get the reading of a word contained in apiData
	 * 
	 * @param apiData
	 * @return Returns the reading of the word contained in apiData
	 */
	public String getReading(JSONObject apiData) {
		JSONObject jpObj = apiData.getJSONArray("data").getJSONObject(0).getJSONArray("japanese").getJSONObject(0);
		return jpObj.getString("reading");
	}

	/**
	 * Gets the word associated with itself in apiData
	 * 
	 * @param apiData
	 * @return Returns the word referenced within the apiData
	 */
	public String getWord(JSONObject apiData) {
		JSONObject jpObj = apiData.getJSONArray("data").getJSONObject(0).getJSONArray("japanese").getJSONObject(0);
		return jpObj.getString("word");
	}

	/**
	 * Gets the meaning associated with the word in apiData
	 * 
	 * @param apiData
	 * @return Returns the meaning of the word contained in apiData
	 */
	public String getMeaning(JSONObject apiData) {
		JSONArray meaningsJSON = apiData.getJSONArray("data").getJSONObject(0).getJSONArray("senses").getJSONObject(0)
				.getJSONArray("english_definitions");
		String meanings = "";
		for (int i = 0; i < meaningsJSON.length(); i++) {
			meanings += meaningsJSON.getString(i) + ", ";
		}
		return meanings;
	}

	/**
	 * Checks whether the word within apiData is common or not
	 * 
	 * @param apiData
	 * @return True if the word is common, false if not
	 */
	public boolean isCommon(JSONObject apiData) {
		JSONObject commonObj = apiData.getJSONArray("data").getJSONObject(0);
		return commonObj.getBoolean("is_common");
	}

	public ArrayList<String> getCommon() {
		return commonWords;
	}
}