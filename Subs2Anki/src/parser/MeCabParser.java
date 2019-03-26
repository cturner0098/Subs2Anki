package parser;

import java.io.*;
import java.util.ArrayList;

public class MeCabParser implements Parser {
	private ArrayList<String> uniqueLines = new ArrayList<>();

	public void parse(ArrayList<String> lines) throws IOException {
		// Get the relative file path of the script used to parse Japanese
		File mecabPython = new File("src/parser/mecab-parse.py");
		String path = mecabPython.getAbsolutePath().replaceAll("\\\\", "/");
		System.out.println("Mecab Python Path: " + path);

		String s = null;
		for (String l : lines) {
			// Create a process that executes commands
			// "python {path to script} {line to parse}"
			Process p = Runtime.getRuntime().exec("python " + path + " " + l.replaceAll("[\\r|\\n|\\s+]", ""));
			BufferedReader pInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			// Read the output from a command line and add all unique entries to uniqueLines
			// Line delimited by a tab
			while ((s = pInput.readLine()) != null) {
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == '	') {
						String sub = s.substring(0, i);
						if (!uniqueLines.contains(sub)) {
							uniqueLines.add(sub);
						}
					}
				}
			}
		}
//		For testing purposes
//		for(String l : uniqueLines) {
//			System.out.println(l);
//		}
	}

	public ArrayList<String> getUniqueLines() {
		return uniqueLines;
	}
}