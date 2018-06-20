package lpoProject.parser;

import java.util.Optional;

public interface Tokenizer extends AutoCloseable {

	TokenType next() throws TokenizerException;

	String tokenString();

	int intValue();

	boolean boolValue();

	Optional optValue();

	TokenType tokenType();

	boolean hasNext();

	public void close() throws TokenizerException;

}