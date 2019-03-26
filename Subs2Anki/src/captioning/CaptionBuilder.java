package captioning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.SAXException;

public class CaptionBuilder {
	public CaptionBuilder(String lang, String videoID) throws IOException, SAXException {
		buildFile(lang, videoID);
	}

	/**
	 * Passes line from URL to parseString and outputs result into a text file
	 * called captions.txt in split lines.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void buildFile(String lang, String videoID) throws IOException, SAXException {
		URL url;
		InputStream is = null;
		BufferedReader br;
		BufferedWriter bw;
		String line;

		try {
			// Opens and reads from URL
			url = new URL("http://video.google.com/timedtext?lang=" + lang + "&v=" + videoID);
			is = url.openStream();
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new FileWriter("captions-" + videoID + "-" + lang + ".txt"));
			
			// Writes lines from parseString to file
			while ((line = br.readLine()) != null) {
				bw.write(parseString(line));	
			}
			bw.close();
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				
			}
		}
		
		// Proof of concept for reading a new line. Implement to sentence variable.
		BufferedReader brt = new BufferedReader(new FileReader("captions-" + videoID + "-" + lang + ".txt"));
		while (brt.readLine() != null) {
			System.out.println(brt.readLine());
		}
		brt.close();
	}

	/**
	 * Scans through a sentence a character at a time, skipping between "<" and ">"
	 * and inserts a newline when it finds escape characters.
	 * 
	 * @param textLine The line of text to search.
	 * @return The formatted string by line.
	 */
	public static String parseString(String textLine) {
		String newString = "";
		boolean newLine;
		char ch = textLine.charAt(0);
		for (int i = 0; i < textLine.length(); i++) {
			if (ch == '<') {
				for (int f = i; f < textLine.length() - 1; i++, f++) {
					ch = textLine.charAt(i);
					if (ch == '/') {
						newLine=true;
					}
					if (ch == '>') {
						if(newLine=true) {
							newString += "\n";
							newLine=false;
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
}
