package parser;

import java.io.IOException;
import java.util.ArrayList;

public interface Parser {
	public void parse(ArrayList<String> lines) throws IOException;
}