package lpoProject;

import static java.lang.System.err;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lpoProject.parser.Parser;
import lpoProject.parser.ParserException;
import lpoProject.parser.StreamParser;
import lpoProject.parser.StreamTokenizer;
import lpoProject.parser.Tokenizer;
import lpoProject.parser.ast.Prog;
import lpoProject.visitors.evaluation.Eval;
import lpoProject.visitors.evaluation.EvaluatorException;
import lpoProject.visitors.typechecking.TypeCheck;
import lpoProject.visitors.typechecking.TypecheckerException;

public class Main {
	public static void main(String[] args) {
		try (Tokenizer tokenizer = new StreamTokenizer(
				args.length > 0 ? new FileReader(args[0]) : new InputStreamReader(System.in))) {
			Parser parser = new StreamParser(tokenizer);
			Prog prog = parser.parseProg();
			prog.accept(new TypeCheck());
			prog.accept(new Eval());
		} catch (ParserException e) {
			err.println("Syntax error: " + e.getMessage());
		} catch (IOException e) {
			err.println("I/O error: " + e.getMessage());
		} catch (TypecheckerException e) {
			err.println("Static error: " + e.getMessage());
		} catch (EvaluatorException e) {
			err.println("Dynamic error: " + e.getMessage());
		} catch (Throwable e) {
			err.println("Unexpected error.");
			e.printStackTrace();
		}
	}
}
