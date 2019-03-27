package main;

import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import anki.Anki;
import captioning.CaptionBuilder;
import parser.JishoParser;
import parser.MeCabParser;

public class Main {
	private static ArrayList<String> commonWords = new ArrayList<>();
	private static ArrayList<String> commonReadings = new ArrayList<>();
	private static ArrayList<String> commonMeanings = new ArrayList<>();
	
	public static void main(String[] args) throws IOException, SAXException, InterruptedException {
		// Video Parameters
		String videoID = "3VQB-xcAeVg";
		String lang = "ja";

		// Builds the captions
		CaptionBuilder cb = new CaptionBuilder(videoID, lang);

		// Parses sentences via MeCab script
		MeCabParser p = new MeCabParser();
		p.parse(cb.getLines());

		// Parses words via Jisho API
		JishoParser jp = new JishoParser();
		jp.parse(p.getUniqueLines());
		commonWords = jp.getCommonWords();
		commonReadings = jp.getCommonReadings();
		commonMeanings = jp.getCommonMeanings();
		
		Anki a = new Anki();

		// Check if deck exists
		String deckName = "videoID-jp";
		if (!a.deckExists("videoID-jp")) {
			a.createDeck(deckName);
			System.out.println("Deck " + deckName + " created!");
			for(int i = 0; i < commonWords.size(); i++) {
				a.createNote(deckName, commonWords.get(i), commonMeanings.get(i), commonReadings.get(i));
			}
		} else {
			System.out.println("Deck already exists!");
		}
	}
}