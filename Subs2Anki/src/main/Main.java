package main;

import java.io.IOException;

import org.xml.sax.SAXException;

import captioning.CaptionBuilder;
import parser.JishoParser;
import parser.MeCabParser;

public class Main {

	public static void main(String[] args) throws IOException, SAXException {
		// Video Parameters 
		String videoID = "3VQB-xcAeVg"; String lang = "ja";
		
		// Builds the captions
		CaptionBuilder cb = new CaptionBuilder(videoID, lang);
		
		// Parses sentences via mecab script
		MeCabParser p = new MeCabParser();
		p.parse(cb.getLines());
		
		// Parses words via jisho api
		JishoParser jp = new JishoParser();
		jp.parse(p.getUniqueLines());
	}
}