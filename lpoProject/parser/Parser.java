package lpoProject.parser;

import lpoProject.parser.ast.Prog;

public interface Parser {

	Prog parseProg() throws ParserException;

}