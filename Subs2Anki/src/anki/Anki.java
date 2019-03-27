package anki;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Anki {
	private File ankiPython;
	private String path;

	public Anki() {
		// Get the relative file path of the script used to parse Japanese
		ankiPython = new File("src/parser/anki.py");
		path = ankiPython.getAbsolutePath().replaceAll("\\\\", "/");
		System.out.println("Anki Python Path: " + path);

	}

	/**
	 * Creates a note in Anki based off of the following parameters:
	 * 
	 * @param deckName   The name of the deck.
	 * @param expression The Kanji/Expression to be added.
	 * @param meaning    The meaning of the Kanji/Expression.
	 * @param reading    The reading of the Kanji/Expression.
	 * @throws IOException
	 */
	public void createNote(String deckName, String expression, String meaning, String reading) throws IOException {
		// Print out commandline used [FOR TESTING]
		// System.out.println(
		// "python " + path + " createNote " + deckName + " " + expression + " " +
		// meaning + " " + reading);

		// Executes:
		// python {path} createNote {deckName} {expression} {meaning} {reading}
		Process p = Runtime.getRuntime()
				.exec("python " + path + " createNote \"" + deckName + "\" \"" + expression + "\" \"" + meaning + "\" \"" + reading + "\"");
		BufferedReader pInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// Prints if the note you're trying to add exists.
		String s = null;
		while ((s = pInput.readLine()) != null) {
			if (s.contains("Exception: cannot create note because it is a duplicate")) {
				System.out.println("Note already exists!");
			}
		}
	}

	/**
	 * Checks if a deck exists based off of the following parameters:
	 * 
	 * @param deckName The name of the deck.
	 * @return True if the deck exists, false if not.
	 * @throws IOException
	 */
	public boolean deckExists(String deckName) throws IOException {
		Process p = Runtime.getRuntime().exec("python " + path + " getDecks");
		BufferedReader pInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String s = null;
		while ((s = pInput.readLine()) != null) {
			if (s.contains(deckName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Creates a deck based off of the following parameter:
	 * 
	 * @param deckName The name of the deck.
	 * @throws IOException
	 */
	public void createDeck(String deckName) throws IOException {
		Process p = Runtime.getRuntime().exec("python " + path + " createDeck " + deckName);
	}
}
