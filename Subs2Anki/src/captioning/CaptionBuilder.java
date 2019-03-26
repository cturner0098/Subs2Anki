package captioning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.xml.sax.SAXException;

public class CaptionBuilder {
	private static ArrayList<String> lines = new ArrayList<>();

	public CaptionBuilder(String videoID, String lang) throws IOException, SAXException {
		getCaptions(videoID, lang);
	}

	/**
	 * Passes line from URL to parseString and outputs result into an ArrayList
	 * called lines.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	private static void getCaptions(String videoID, String lang) throws IOException, SAXException {
		URL url = new URL("http://video.google.com/timedtext?lang=" + lang + "&v=" + videoID);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;

		System.out.println(url.toString());

		// Adds lines from parseString to ArrayList lines
		while ((line = br.readLine()) != null) {
			lines.add(parseXML(line));
			// System.out.println(parseString(line));
		}
	}

	/**
	 * Scans through a sentence a character at a time, skipping between "<" and ">"
	 * and inserts a newline when it finds escape characters.
	 * 
	 * @param textLine The line of text to search.
	 * @return The formatted string by line.
	 */
	private static String parseXML(String textLine) {
		String newString = "";
		boolean newLine = false;
		char ch = textLine.charAt(0);
		for (int i = 0; i < textLine.length(); i++) {
			if (ch == '<') {
				for (int f = i; f < textLine.length() - 1; i++, f++) {
					ch = textLine.charAt(i);
					if (ch == '/') {
						newLine = true;
					}
					if (ch == '>') {
						if (newLine) {
							newString += "\n"; // --removed because it messes with MeCab text parsing [RE ADDED MAYBE
												// TEMPORARILY LOL IDK]
							newLine = false;
						}
						i++;
						break;
					}
				}
			}
			ch = textLine.charAt(i);
			if (ch != '<' && ch != '>') {
				newString += ch;
			}
		}
		return newString;
	}

	public ArrayList<String> getLines() {
		return lines;
	}
}